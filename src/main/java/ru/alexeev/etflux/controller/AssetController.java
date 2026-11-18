package ru.alexeev.etflux.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class AssetController {
    @GetMapping
    private String index() {
        return "Hello World";
    }
}
