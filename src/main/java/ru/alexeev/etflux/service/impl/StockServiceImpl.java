package ru.alexeev.etflux.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexeev.etflux.client.TbankInstrumentsApiClient;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dto.outsider.OutsideStockResponse;
import ru.alexeev.etflux.dto.request.StockRequestDto;

import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.UserAssetPosition;
import ru.alexeev.etflux.entity.UserProfile;
import ru.alexeev.etflux.entity.assets.Asset;
import ru.alexeev.etflux.entity.assets.Stock;
import ru.alexeev.etflux.exception.InstrumentsNotFoundException;
import ru.alexeev.etflux.mapper.StockMapper;
import ru.alexeev.etflux.repository.ProfileRepository;
import ru.alexeev.etflux.repository.StockRepository;
import ru.alexeev.etflux.repository.UserAssetPositionRepository;
import ru.alexeev.etflux.repository.UserRepository;
import ru.alexeev.etflux.service.AssetPositionsActualizer;
import ru.alexeev.etflux.service.StockService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private final StockMapper stockMapper;
    private final UserRepository userRepository;
    private final UserAssetPositionRepository userAssetPositionRepository;
    private final TbankInstrumentsApiClient client;
    private final AssetPositionsActualizer assetPositionsActualizer;
    private final ProfileRepository profileRepository;

    @Override
    public Stock getStockByTicker(String ticker) {
        return stockRepository.findByTicker(ticker)
                .orElseGet(() -> fetchAndSaveStockFromApi(ticker));
    }

    @Override
    public Stock getStockWithPriceByTicker(String ticker) {
        return null;
    }

    public Stock fetchAndSaveStockFromApi(String ticker) {
        try {
            List<OutsideStockResponse> response = client.getListOfInstruments(ticker);
            Stock stock = stockMapper.outsideToStock(response.get(0));
            return stockRepository.save(stock);
        } catch (InstrumentsNotFoundException e) {
            throw new RuntimeException("Instrument not found", e);
        }
    }

    @Transactional
    @Override
    public AssetPositionResponseDto<Stock> addStock(StockRequestDto stockRequestDto, User user) {
        Stock stock = getOrCreateStock(stockRequestDto);
        stock.setType(AssetTypeName.STOCK);


        if (user.getProfile() == null){
            var userProfile = new UserProfile();
            userProfile.setUser(user);
            var savedUserProfile = profileRepository.save(userProfile);
            user.setProfile(savedUserProfile);
        }
        UserProfile profile = user.getProfile();

        Optional<UserAssetPosition> positionOptional = userAssetPositionRepository
                .findByUserProfileAndAsset(profile, stock);

        UserAssetPosition position;
        position = positionOptional.orElseGet(() -> createNewPosition(profile, stock));
        boolean isSell = stockRequestDto.getTransactionType().name().equalsIgnoreCase("sell");
        position.addUnits(stockRequestDto.getQuantity(), stockRequestDto.getBuyPrice(), isSell); //обновление средней цены покупки и количетсов купленных бумаг
        userAssetPositionRepository.save(position);

        try {
            return assetPositionsActualizer.actualizeStock(position).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Stock> getStocksByTicker(String ticker) {
        return List.of();
    }

    @Override
    public Stock getStockShareByTicker(String ticker) {
        return null;
    }

    private Stock getOrCreateStock(StockRequestDto dto) {
        if (dto.getType() == null){
            dto.setType("share");
        }
        Optional<Stock> stock = stockRepository.findByFigi(dto.getFigi());
        return stock
                .or(() -> {
                    List<Stock> list = stockRepository.findByTickerContainingAndStockType(dto.getTicker(), dto.getType().toLowerCase());
                    if (list.isEmpty()) {
                        return Optional.empty();
                    }
                    return Optional.of(list.get(0));
                })
                .orElseGet(() -> {
                    try {
                        System.out.println("ticker " + dto.getTicker());
                        OutsideStockResponse resp = client.getListOfInstruments(dto.getTicker())
                                .stream()
                                .filter(inst -> inst.getInstrumentType().toLowerCase().equals(dto.getType()))
                                .findFirst().get();
                        return stockRepository.save(stockMapper.outsideToStock(resp));
                    } catch (InstrumentsNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private UserAssetPosition createNewPosition(UserProfile profile, Asset asset) {
        UserAssetPosition position = new UserAssetPosition();
        position.setUserProfile(profile);
        position.setAsset(asset);
        position.setQuantity(BigDecimal.ZERO);
        position.setAveragePrice(BigDecimal.ZERO);
        position.setLastUpdated(LocalDateTime.now());
        profile.getAssetPositions().add(position);
        return position;
    }

}
