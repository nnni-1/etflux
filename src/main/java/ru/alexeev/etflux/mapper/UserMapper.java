package ru.alexeev.etflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import ru.alexeev.etflux.web.controller.admin.dto.UserAdminDto;
import ru.alexeev.etflux.dictionary.UserStatus;
import ru.alexeev.etflux.dto.UserRegistrationForm;
import ru.alexeev.etflux.entity.User;

import java.util.List;


@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id", ignore = true) // Игнорируем поле id, так как оно генерируется базой данных
    @Mapping(target = "hashPassword", source = "password") // Маппим пароль из формы в hashPassword
    @Mapping(target = "roles", ignore = true) // Игнорируем роли, если они устанавливаются отдельно
    User mapToUser(UserRegistrationForm registrationForm);

    @Mapping(target = "active", source = "userStatus", qualifiedByName = "statusToActive")
    UserAdminDto mapToUserAdminDto(User user);

    List<UserAdminDto> toDtoList(List<User> users);

    @Named("statusToActive")
    static boolean mapStatus(UserStatus status) {
        return status == UserStatus.ACTIVE;
    }
}
