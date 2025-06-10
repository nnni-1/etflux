package ru.alexeev.etflux.exception;

public class StockPriceNotFoundException extends RuntimeException {
    public StockPriceNotFoundException(String message) {
        super(message);
    }
}
