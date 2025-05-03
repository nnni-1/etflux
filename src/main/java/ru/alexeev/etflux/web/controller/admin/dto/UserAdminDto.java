package ru.alexeev.etflux.web.controller.admin.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.alexeev.etflux.entity.UserRole;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class UserAdminDto {
    private Long id;
    private String username;
    private String email;
    private List<UserRole> roles;
    private LocalDateTime createdAt;
    private Boolean active;
}
