package ru.alexeev.etflux.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alexeev.etflux.entity.UserProfile;

public interface ProfileRepository extends JpaRepository<UserProfile, Long> {
}
