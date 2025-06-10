package ru.alexeev.etflux.service;

import ru.alexeev.etflux.entity.Transaction;
import ru.alexeev.etflux.entity.UserProfile;
import ru.alexeev.etflux.entity.assets.Asset;

public interface AssetService {
    void updateAssetPosition(Transaction transaction);
    void createOrFindAsset(Asset asset, UserProfile userProfile);
}
