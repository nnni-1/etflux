package ru.alexeev.etflux.service;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.alexeev.etflux.web.controller.admin.dto.UserAdminDto;
import ru.alexeev.etflux.dictionary.UserStatus;
import ru.alexeev.etflux.dto.UserExistsDto;
import ru.alexeev.etflux.entity.User;
import ru.alexeev.etflux.entity.UserRole;
import ru.alexeev.etflux.exception.EntityNotFoundException;
import ru.alexeev.etflux.mapper.UserMapper;
import ru.alexeev.etflux.repository.UserRepository;
import ru.alexeev.etflux.repository.UserRoleRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class UserService {

    private UserRepository userRepository;
    private UserRoleRepository roleRepository;
    private UserMapper userMapper;

    public UserExistsDto userExistsByUsername(String username) {
        return UserExistsDto.builder()
                .userExists(userRepository.existsByUsername(username))
                .username(username)
                .build();
    }

    public Page<UserAdminDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> userMapper.mapToUserAdminDto(user));
    }

    public Page<UserAdminDto> findAll(String username, String email, Pageable pageable) {
        if (email != null && username != null) {
            return userRepository.findByUsernameContainingAndEmailContainingIgnoreCase(username, email, pageable)
                    .map(user -> userMapper.mapToUserAdminDto(user));
        } else if (email != null && !email.isEmpty()) {
            return userRepository.findByEmailContainingIgnoreCase(email, pageable)
                    .map(user -> userMapper.mapToUserAdminDto(user));
        } else if (username != null && !username.isEmpty()) {
            return userRepository.findByUsernameContainingIgnoreCase(username, pageable)
                    .map(user -> userMapper.mapToUserAdminDto(user));
        } else {
            return userRepository.findAll(pageable)
                    .map(user -> userMapper.mapToUserAdminDto(user));
        }
    }

    public UserAdminDto findById(Long id) {
        return userRepository.findById(id)
                .map(user -> userMapper.mapToUserAdminDto(user))
                .orElseThrow(() -> new EntityNotFoundException("user", "id", String.valueOf(id)));
    }

    @Transactional
    public void blockUser(Long userId) {
        User user = findUser(userId);
        user.setUserStatus(UserStatus.BLOCKED);
    }

    @Transactional
    public void addRole(Long userId, Integer roleId) {
        User user = findUser(userId);
        List<UserRole> roles = user.getRoles();
        UserRole userRole = roleRepository
                .findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("role", "id", String.valueOf(roleId)));

        if (!roles.contains(userRole)){
            roles.add(userRole);
        }
    }

    @Transactional
    public void changeStatus(Long userId, String newStatus) {
        User user = findUser(userId);
        user.setUserStatus(UserStatus.valueOf(newStatus));
    }

    @Transactional
    public void deleteRole(Long userId, Integer roleId) {
        User user = findUser(userId);
        List<UserRole> roles = user.getRoles();
        UserRole role = roleRepository
                .findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("role", "id", String.valueOf(roleId)));
        roles.remove(role);
    }

    public List<UserAdminDto> findByUsername(String username, int pageNum, int size){
        Pageable page = PageRequest.of(pageNum, size);
        return userRepository.findByUsernameContainingIgnoreCase(username, page)
                .stream()
                .map(userMapper::mapToUserAdminDto)
                .toList();
    }

    private User findUser(Long userId){
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("user", "id", String.valueOf(userId)));
    }

    public List<UserAdminDto> findByEmail(String email, int pageNum, int size) {
        Pageable page = PageRequest.of(pageNum, size);
        return userRepository.findByEmailContainingIgnoreCase(email, page)
                .stream()
                .map(userMapper::mapToUserAdminDto)
                .toList();
    }
}
