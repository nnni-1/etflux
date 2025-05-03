package ru.alexeev.etflux.dto;

import lombok.Data;
import lombok.experimental.Accessors;


@Data
@Accessors
public class UsernamePasswordDto {

    private String username;
    private String password;

}
