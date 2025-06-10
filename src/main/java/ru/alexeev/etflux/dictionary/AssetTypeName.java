package ru.alexeev.etflux.dictionary;

import lombok.AllArgsConstructor;


public enum AssetTypeName {
    BOND("bond"),
    SHARE("share"),
    STOCK("stock"),
    BANK_DEPOSIT("bank_deposit"),
    NATIONAL_CURRENCY("national_currency"),
    CRYPTO_CURRENCY("crypto_currency");

    private final String name;

    AssetTypeName(String name) {
        this.name = name;
    }
    public static AssetTypeName fromName(String name) {
        for (AssetTypeName type : AssetTypeName.values()) {
            if (type.name.toLowerCase().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
