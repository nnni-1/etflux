package ru.alexeev.etflux.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.alexeev.etflux.validation.PasswordsMatch;

@Data
@Accessors(chain = true)
@PasswordsMatch
public class UserRegistrationForm {
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "username must contain only letters, digits and _")
    @NotBlank(message = "username is required")
    @Size(min = 3, max = 50, message = "username must have length between 3 and 50")
    private String username;
    @NotBlank(message = "email us required")
    @Email(message = "invalid email format")
    private String email;
    @NotBlank(message = "password is required")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>]).{8,}$",
             message = "password must have at least 8 symbols and include: 1 big letter, 1 small letter, 1 digit, 1 special symbol")
    private String password;
    private String confirmPassword;

}