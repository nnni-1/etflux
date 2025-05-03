package ru.alexeev.etflux.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class AssetController {
    @GetMapping
    @ResponseBody
    private String index() {
        return "Hello World";
    }
}
