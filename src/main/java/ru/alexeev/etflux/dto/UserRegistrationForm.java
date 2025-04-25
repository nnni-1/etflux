package ru.alexeev.etflux.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserRegistrationForm {

    private String username;
    private String email;
    private String password;
    private String confirmPassword;

}