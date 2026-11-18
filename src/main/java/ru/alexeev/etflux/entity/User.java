package ru.alexeev.etflux.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.dictionary.UserRoleEnum;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String hashPassword;
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", unique = false))
    private List<UserRole> roles;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (roles == null){
            roles = new ArrayList<>();
        }
        createdAt = LocalDateTime.now();
    }
}
