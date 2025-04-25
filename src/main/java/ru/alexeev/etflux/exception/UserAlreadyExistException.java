package ru.alexeev.etflux.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String userExist) {
        super(userExist);
    }
}
