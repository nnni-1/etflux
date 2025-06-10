package ru.alexeev.etflux.service;

import org.springframework.stereotype.Service;
import ru.alexeev.etflux.entity.UserProfile;
import ru.alexeev.etflux.entity.assets.Stock;

@Service
public class RefreshService {
    private StockService stockService;

    public UserProfile refreshProfile(UserProfile userProfile) {
        userProfile.getAssetsByType(Stock.class);
        return userProfile;
    }
}
