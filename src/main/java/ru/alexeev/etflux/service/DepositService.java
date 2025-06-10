package ru.alexeev.etflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dto.request.DepositRequestDto;
import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.UserAssetPosition;
import ru.alexeev.etflux.entity.assets.Deposit;
import ru.alexeev.etflux.mapper.DepositMapper;
import ru.alexeev.etflux.repository.UserAssetPositionRepository;
import ru.alexeev.etflux.repository.UserRepository;

import java.util.List;

@AllArgsConstructor
@Service
public class DepositService {

    private final UserAssetPositionRepository userAssetPositionRepository;
    private DepositMapper depositMapper;
    private AssetPositionsActualizer assetPositionsActualizer;
    private UserRepository userRepository;

    public AssetPositionResponseDto<Deposit> addDeposit(DepositRequestDto depositRequest, User user) {
        Deposit deposit = depositMapper.toDeposit(depositRequest);
        deposit.setType(AssetTypeName.BANK_DEPOSIT);
        UserAssetPosition newDepositPosition = UserAssetPosition.builder()
                .asset(deposit)
                .userProfile(user.getProfile())
                .averagePrice(deposit.getPrice())
                .build();

        return assetPositionsActualizer.actulizeDeposits(List.of(newDepositPosition)).get(0);
    }
    
}
