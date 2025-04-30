package ru.alexeev.etflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexeev.etflux.dto.UserRegistrationForm;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.UserRole;
import ru.alexeev.etflux.exception.UserAlreadyExistException;
import ru.alexeev.etflux.mapper.UserMapper;
import ru.alexeev.etflux.repository.UserRepository;
import ru.alexeev.etflux.repository.UserRoleRepository;

import java.util.List;

import static ru.alexeev.etflux.dictionary.UserRoleEnum.USER;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
//    private final ConfirmService confirmService;

    @Transactional
    public void createUser(UserRegistrationForm form) {
        userRepository.findByUsername(form.getUsername())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("user exist");
                });

        userRepository.findByEmail(form.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("user exist");
                });

        String encode = passwordEncoder.encode(form.getPassword());
        User user = userMapper.mapToUser(form);
        user.setHashPassword(encode);
        userRepository.save(user);
        UserRole userRole = userRoleRepository.findByRole("USER");
        user.setRoles(List.of(userRole));
//        confirmService.confirm(user.getUsername(), user.getEmail());
    }
}
