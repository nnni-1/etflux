package ru.alexeev.etflux.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.alexeev.etflux.entity.Stock;
import ru.alexeev.etflux.service.StockService;

@Controller
@RequiredArgsConstructor
public class StockController {
    private StockService stockService;

    @GetMapping("/stocks/{ticker}")
    public Stock stocks(@PathVariable String ticker) {
        return stockService.getStockByTicker(ticker);
    }
}
