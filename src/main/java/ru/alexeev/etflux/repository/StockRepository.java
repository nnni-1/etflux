package ru.alexeev.etflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.entity.assets.Stock;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByTicker(String ticker);
    List<Stock> save(List<Stock> stocks);
    Optional<Stock> findByFigi(String figi);
    List<Stock> findByTickerContainingAndStockType(String ticker, String type);
}
