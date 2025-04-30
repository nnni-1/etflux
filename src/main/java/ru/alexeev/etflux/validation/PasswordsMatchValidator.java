package ru.alexeev.etflux.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.alexeev.etflux.dto.UserRegistrationForm;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, UserRegistrationForm> {

    @Override
    public boolean isValid(UserRegistrationForm form, ConstraintValidatorContext constraintValidatorContext) {
        return form.getPassword() != null
                && form.getPassword().equals(form.getConfirmPassword());
    }
}
