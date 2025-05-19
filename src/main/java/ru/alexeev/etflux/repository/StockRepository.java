package ru.alexeev.etflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeev.etflux.entity.Stock;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByTicker(String ticker);
}
