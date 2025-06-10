package ru.alexeev.etflux.service;

import ru.alexeev.etflux.dto.request.StockRequestDto;
import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.dto.response.StockResponseDto;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.assets.Stock;

import java.util.List;

public interface StockService {
    Stock getStockByTicker(String ticker);
    Stock getStockWithPriceByTicker(String ticker);
    AssetPositionResponseDto<Stock> addStock(StockRequestDto stockRequestDto, User user);
    List<Stock> getStocksByTicker(String ticker);
    Stock getStockShareByTicker(String ticker);
}
