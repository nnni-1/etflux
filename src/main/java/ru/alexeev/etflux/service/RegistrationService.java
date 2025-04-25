package ru.alexeev.etflux.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexeev.etflux.dto.UserRegistrationForm;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.exception.UserAlreadyExistException;
import ru.alexeev.etflux.mapper.UserMapper;
import ru.alexeev.etflux.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
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

//        confirmService.confirm(user.getUsername(), user.getEmail());
    }
}
