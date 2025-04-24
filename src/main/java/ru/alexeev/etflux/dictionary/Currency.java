package ru.alexeev.etflux.dictionary;

public enum Currency {
    USD("USD"),
    RUB("RUB"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    CHF("CHF"),
    JPY("JPY"),
    CNY("CNY"),
    TRY("TRY");
    private String currency;
    Currency(String currency){
        this.currency = currency;
    }
}
