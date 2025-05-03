package ru.alexeev.etflux.dictionary;

public enum UserStatus {
    ACTIVE("ACTIVE"),
    BLOCKED("BLOCKED"),
    INACTIVE("INACTIVE"),
    DELETED("DELETED");
    private final String value;
    UserStatus(String status){
        this.value = status;
    }

}
