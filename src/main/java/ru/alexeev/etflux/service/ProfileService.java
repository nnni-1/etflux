package ru.alexeev.etflux.service;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dto.request.DepositRequestDto;
import ru.alexeev.etflux.dto.request.StockRequestDto;
import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.UserAssetPosition;
import ru.alexeev.etflux.entity.assets.Deposit;
import ru.alexeev.etflux.entity.assets.Stock;
import ru.alexeev.etflux.repository.UserAssetPositionRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProfileService {

    private final UserAssetPositionRepository userAssetPositionRepository;
    private StockService stockService;
    private DepositService depositService;
    private AssetPositionsActualizer assetPositionsActualizer;
    private RedisTemplate<String, String> redisTemplate;

    public AssetPositionResponseDto<Deposit> addDeposit(DepositRequestDto depositRequest, User user) {
        return depositService.addDeposit(depositRequest, user);
    }

    public AssetPositionResponseDto<Stock> addStock(StockRequestDto stockRequest, User user){
        return stockService.addStock(stockRequest, user);
    }

    public List<AssetPositionResponseDto<Deposit>> getDeposits(User user){
        return assetPositionsActualizer.actulizeDeposits(user.getProfile().getAssetsByType(Deposit.class));
    }

    public List<AssetPositionResponseDto<Stock>> getStocks(User user){
        try {
            Future<List<AssetPositionResponseDto<Stock>>> future = assetPositionsActualizer.actulizeStocks(user.getProfile().getAssetsByType(Stock.class));
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, List<AssetPositionResponseDto<?>>> getAssets(User user) {

        Map<String, List<AssetPositionResponseDto<?>>> assetsByType = new HashMap<>();

        List<UserAssetPosition> assetPositions = userAssetPositionRepository.findByUserProfile(user.getProfile());

        Map<Class<?>, List<UserAssetPosition>> groupedByAssetType = assetPositions.stream()
                .collect(Collectors.groupingBy(
                        position -> position.getAsset().getClass()
                ));

        for (Map.Entry<Class<?>, List<UserAssetPosition>> entry : groupedByAssetType.entrySet()) {
            Class<?> assetType = entry.getKey();
            List<UserAssetPosition> positions = entry.getValue();

            AssetTypeName assetTypeName = positions.get(0).getAsset().getType();

            try {
                List<AssetPositionResponseDto<?>> actualizedAssets;
                if (assetType.equals(Stock.class)) {
                    actualizedAssets = (List<AssetPositionResponseDto<?>>) (List<?>) getStocks(user);
                    System.out.println(actualizedAssets + " stocks in profileService");
                } else if (assetType.equals(Deposit.class)) {
                    actualizedAssets = (List<AssetPositionResponseDto<?>>) (List<?>) getDeposits(user);
                    System.out.println(actualizedAssets + " deposits in profileService");
                } else {
                    throw new UnsupportedOperationException("Unsupported asset type: " + assetType.getSimpleName());
                }

                assetsByType.put(assetTypeName.name(), actualizedAssets);

            } catch (ClassCastException e) {
                throw new RuntimeException("Ошибка при актуализации активов типа " + assetTypeName, e);
            }
        }

        return assetsByType;
    }

    public BigDecimal getTotal(User user) {
        BigDecimal total = BigDecimal.ZERO;
        var allAssetPositions = getAssets(user);
        allAssetPositions.forEach((key, value) -> value.forEach(position -> total.add(position.getAverageBuyPrice())));
        return total;
    }
}
