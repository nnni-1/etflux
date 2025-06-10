package ru.alexeev.etflux.listener;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.data.querydsl.binding.QuerydslPredicateBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.alexeev.etflux.dictionary.UserRoleEnum;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.UserRole;
import ru.alexeev.etflux.repository.UserRepository;
import ru.alexeev.etflux.repository.UserRoleRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ApplicationReadyEventListener {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository roleRepository;

    @Profile("dev")
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationReady() {
        UserRole userRole;
        if (roleRepository.count() == 0){
            userRole = roleRepository.save(new UserRole("ADMIN"));
            roleRepository.save(new UserRole("USER"));
            roleRepository.save(new UserRole("MODERATOR"));
        } else {
            userRole = roleRepository.findByRole("ADMIN");
        }
        if (userRepository.count() == 0){
            userRepository.save(User.builder()
                        .email("nikita@gmail.com")
                        .roles(List.of(userRole))
                        .hashPassword(passwordEncoder.encode("aaaaaA1*"))
                        .username("nikita")
                        .build());
        }

    }

}
