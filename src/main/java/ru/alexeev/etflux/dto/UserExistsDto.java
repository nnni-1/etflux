package ru.alexeev.etflux.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserExistsDto {
    private boolean userExists;
    private String username;
    private String userId;
}
