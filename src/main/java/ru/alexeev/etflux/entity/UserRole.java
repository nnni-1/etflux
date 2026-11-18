package ru.alexeev.etflux.entity;

import jakarta.persistence.*;
import lombok.*;

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
            joinColumns = @JoinColumn(name = "role_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "user_id", unique = false))
    private List<User> users;

    @Column(unique = true)
    private String role;

    public UserRole(String role) {
        this.role = role;
    }
}
