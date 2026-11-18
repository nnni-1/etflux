package ru.alexeev.etflux.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.alexeev.etflux.dto.UserRegistrationForm;
import ru.alexeev.etflux.service.RegistrationService;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService service;
    @GetMapping
    public String getPage(Model model) {
        model.addAttribute("registrationForm", new UserRegistrationForm());
        return "registration";
    }

    @PostMapping
    public String createUser(
            @Valid UserRegistrationForm registrationForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }

        service.createUser(registrationForm);

        return "redirect:/hello";
    }

}
