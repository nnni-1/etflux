package ru.alexeev.etflux.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import ru.alexeev.etflux.dictionary.UserRoleEnum;
import ru.alexeev.etflux.dictionary.UserStatus;

import java.io.Serial;
import java.io.Serializable;
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
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 9428234628432L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    private String hashPassword;
    @Column(nullable = false, unique = true)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", unique = false))
    @JsonManagedReference
    private List<UserRole> roles;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserProfile profile;

    @PrePersist
    protected void onCreate() {
        if (roles == null){
            roles = new ArrayList<>();
        }
        userStatus = UserStatus.ACTIVE;
        createdAt = LocalDateTime.now();
    }

    public boolean isActive(){
        return userStatus == UserStatus.ACTIVE;
    }

    public void block(){
        userStatus = UserStatus.BLOCKED;
    }

    @Override
    public String toString() {
        return """
                {
                "id": "%d",
                "username": "%s",
                "email": "%s",
                "roles": "%s",
                "createdAt": "%s",
                "userStatus": "%s",
                }
                """.formatted(id, username, email, roles, createdAt, userStatus);
    }
}
