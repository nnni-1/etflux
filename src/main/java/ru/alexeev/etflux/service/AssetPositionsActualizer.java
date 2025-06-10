package ru.alexeev.etflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import ru.alexeev.etflux.client.TbankInstrumentsApiClient;
import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.entity.UserAssetPosition;
import ru.alexeev.etflux.entity.assets.Asset;
import ru.alexeev.etflux.entity.assets.Deposit;
import ru.alexeev.etflux.entity.assets.Stock;
import ru.alexeev.etflux.mapper.PositionMapper;
import ru.alexeev.etflux.repository.UserAssetPositionRepository;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AssetPositionsActualizer {
    private final UserAssetPositionRepository userAssetPositionRepository;
    private final TbankInstrumentsApiClient tbankInstrumentsApiClient;

    public List<AssetPositionResponseDto<Deposit>> actulizeDeposits(List<UserAssetPosition> userAssetPositions){
        List<AssetPositionResponseDto<Deposit>> actualDeposits = new ArrayList<>();

        for (UserAssetPosition userAssetPosition : userAssetPositions) {
            if (userAssetPosition.getAsset() instanceof Deposit deposit) {
                Integer capitalizationInDays = deposit.getCapitalizationDays();
                boolean hasCapitalization = deposit.getCapitalization();

                long daysBetween = ChronoUnit.DAYS.between(deposit.getRegistrationDate(), LocalDate.now());

                BigDecimal percentYear = BigDecimal.valueOf(deposit.getPercentYear())
                        .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);

                BigDecimal initialAmount = userAssetPosition.getAveragePrice();

                BigDecimal totalAmount;
                if (hasCapitalization && capitalizationInDays != null && capitalizationInDays > 0) {
                    int periods = (int) (daysBetween / capitalizationInDays);
                    BigDecimal periodRate = percentYear.multiply(BigDecimal.valueOf(capitalizationInDays))
                            .divide(BigDecimal.valueOf(365), RoundingMode.HALF_UP);

                    totalAmount = initialAmount.multiply(
                            BigDecimal.ONE.add(periodRate).pow(periods, MathContext.DECIMAL128)
                    );
                } else {
                    BigDecimal accruedInterest = initialAmount
                            .multiply(percentYear)
                            .multiply(BigDecimal.valueOf(daysBetween))
                            .divide(BigDecimal.valueOf(365), RoundingMode.HALF_UP);

                    totalAmount = initialAmount.add(accruedInterest);
                }

                UserAssetPosition actualUserAssetPosition = UserAssetPosition.builder()
                        .asset(deposit)
                        .averagePrice(totalAmount.stripTrailingZeros())
                        .build();

                actualDeposits.add(PositionMapper.toAssetDto(actualUserAssetPosition, deposit));
                userAssetPosition.setLastUpdated(LocalDateTime.now());
                userAssetPosition.setAveragePrice(totalAmount.stripTrailingZeros());
            }
        }

        return actualDeposits;
    }

    @Async
    public Future<List<AssetPositionResponseDto<Stock>>> actulizeStocks(List<UserAssetPosition> assetsByType) {
        try {
            List<Stock> stocksToFetch = assetsByType.stream()
                    .filter(a -> a.getAsset() instanceof Stock)
                    .filter(asset -> Duration.between(asset.getLastUpdated(), LocalDateTime.now()).toSeconds() > 10)
                    .map(asset -> (Stock) asset.getAsset())
                    .toList();

            if (stocksToFetch.isEmpty()) {
                return CompletableFuture.completedFuture(
                        assetsByType.stream()
                                .map(p -> buildAssetPositionDto(p, (Stock) p.getAsset()))
                                .toList()
                );
            }

            List<Stock> stocks = tbankInstrumentsApiClient.getListOfInstrumentsWithPrices(stocksToFetch);
            Map<String, Stock> stockMap = stocks.stream()
                    .collect(Collectors.toMap(Stock::getFigi, Function.identity()));

            List<AssetPositionResponseDto<Stock>> result = new ArrayList<>();

            for (UserAssetPosition position : assetsByType) {
                Asset asset = position.getAsset();
                if (!(asset instanceof Stock stock)) continue;

                Stock updatedStock = stockMap.getOrDefault(stock.getFigi(), stock);

                boolean needsUpdate = Duration.between(position.getLastUpdated(), LocalDateTime.now()).toSeconds() > 10;
                if (!needsUpdate) {
                    updatedStock = stock;
                }

                result.add(buildAssetPositionDto(position, updatedStock));
            }

            return CompletableFuture.completedFuture(result);

        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    @Async
    public Future<AssetPositionResponseDto<Stock>> actualizeStock(UserAssetPosition asset) {
        if (!(asset.getAsset() instanceof Stock stock)) {
            return CompletableFuture.completedFuture(null);
        }

        try {
            Stock updatedStock = tbankInstrumentsApiClient.getInstrumentWithPrice(stock);
            AssetPositionResponseDto<Stock> dto = buildAssetPositionDto(asset, updatedStock);
            return CompletableFuture.completedFuture(dto);

        } catch (Exception e) {
            return CompletableFuture.failedFuture(new RuntimeException("Error while updating single stock position", e));
        }
    }

    private AssetPositionResponseDto<Stock> buildAssetPositionDto(UserAssetPosition position, Stock updatedStock) {
        AssetPositionResponseDto<Stock> stockPosition = PositionMapper.toAssetDto(position, updatedStock);

        stockPosition.setLastUpdated(LocalDateTime.now());
        stockPosition.setAsset(updatedStock);
        stockPosition.setCurrentPricePerUnit(updatedStock.getPrice());

        if (stockPosition.getAverageBuyPrice().compareTo(stockPosition.getCurrentPricePerUnit()) > 0) {
            BigDecimal priceDifference = stockPosition.getAverageBuyPrice().subtract(stockPosition.getCurrentPricePerUnit());
            stockPosition.setGrowthInCurrencyUnits(priceDifference.negate());
            BigDecimal growthInPercents = priceDifference.divide(stockPosition.getAverageBuyPrice(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            stockPosition.setGrowthInPercents(growthInPercents.negate());
        } else {
            BigDecimal priceDifference = stockPosition.getCurrentPricePerUnit().subtract(stockPosition.getAverageBuyPrice());
            stockPosition.setGrowthInCurrencyUnits(priceDifference);
            BigDecimal growthInPercents = priceDifference.divide(stockPosition.getAverageBuyPrice(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            stockPosition.setGrowthInPercents(growthInPercents);
        }

        return stockPosition;
    }
}
