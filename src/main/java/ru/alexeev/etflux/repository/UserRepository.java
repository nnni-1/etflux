package ru.alexeev.etflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeev.etflux.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Object> findByEmail(String email);
}
