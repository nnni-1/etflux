package ru.alexeev.etflux.web.controller.admin;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.alexeev.etflux.security.jwt.JwtAuthenticationToken;

@Controller
public class AdminController {

    @GetMapping("/admin/users")
    public String usersPage(Model model){
        return "admin";
    }
}
