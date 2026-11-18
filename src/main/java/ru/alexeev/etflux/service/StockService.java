package ru.alexeev.etflux.service;

import ru.alexeev.etflux.entity.Stock;

public interface StockService {
    Stock getStockByTicker(String ticker);
}
