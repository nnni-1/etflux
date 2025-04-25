package ru.alexeev.etflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.alexeev.etflux.dto.UserRegistrationForm;
import ru.alexeev.etflux.entity.User;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true) // Игнорируем поле id, так как оно генерируется базой данных
    @Mapping(target = "hashPassword", source = "password") // Маппим пароль из формы в hashPassword
    @Mapping(target = "roles", ignore = true) // Игнорируем роли, если они устанавливаются отдельно
    User mapToUser(UserRegistrationForm registrationForm);
}
