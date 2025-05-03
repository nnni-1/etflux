package ru.alexeev.etflux.web.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.alexeev.etflux.dto.UserExistsDto;
import ru.alexeev.etflux.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UsersController {

    private UserService userService;

    @GetMapping
    public UserExistsDto userExistsBuUsername(@RequestParam("username") String username) {
        return userService.userExistsByUsername(username);
    }
}
