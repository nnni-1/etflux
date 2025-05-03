package ru.alexeev.etflux.exception;

public class EntityNotFoundException extends RuntimeException {
    private static final String message = "%s with %s %s not found";
    public EntityNotFoundException(String entity, String nameOfField, String uniqueValue) {
        super(message.formatted(entity, nameOfField, uniqueValue));
    }
}
