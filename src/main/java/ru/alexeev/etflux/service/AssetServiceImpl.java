package ru.alexeev.etflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexeev.etflux.entity.Transaction;
import ru.alexeev.etflux.entity.UserProfile;
import ru.alexeev.etflux.entity.assets.Asset;
import ru.alexeev.etflux.repository.TransactionRepository;

@Service
@AllArgsConstructor
public class AssetServiceImpl implements AssetService {

    private TransactionRepository transactionRepository;
//    private AssetRepository assetRepository;

    @Override
    public void updateAssetPosition(Transaction transaction) {
        transactionRepository.save(transaction);


    }

    @Override
    public void createOrFindAsset(Asset asset, UserProfile userProfile) {

    }
}
