package ru.alexeev.etflux.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.dictionary.UserRoleEnum;

import java.util.List;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", unique = false))
    private List<User> users;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;
}
