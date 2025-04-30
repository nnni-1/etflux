package ru.alexeev.etflux.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ru.alexeev.etflux.dictionary.UserRoleEnum;
import ru.alexeev.etflux.entity.UserRole;
import ru.alexeev.etflux.repository.UserRoleRepository;

@Component
@RequiredArgsConstructor
public class ApplicationReadyEventListener {

    private final UserRoleRepository userRoleRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        userRoleRepository.deleteAll();
        userRoleRepository.save(new UserRole("ADMIN"));
        userRoleRepository.save(new UserRole("USER"));
        userRoleRepository.save(new UserRole("MODERATOR"));
    }

}
