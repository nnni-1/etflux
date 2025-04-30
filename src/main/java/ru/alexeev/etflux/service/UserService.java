package ru.alexeev.etflux.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alexeev.etflux.dto.UserExistsDto;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserExistsDto userExistsByUsername(String username) {
        return UserExistsDto.builder()
                .userExists(userRepository.existsByUsername(username))
                .username(username)
                .build();
    }
}
