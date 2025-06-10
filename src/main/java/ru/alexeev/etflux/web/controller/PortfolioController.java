package ru.alexeev.etflux.web.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alexeev.etflux.dto.request.DepositRequestDto;
import ru.alexeev.etflux.dto.request.StockRequestDto;
import ru.alexeev.etflux.security.details.UserDetailsImpl;
import ru.alexeev.etflux.service.ProfileService;

@Controller
@AllArgsConstructor
@RequestMapping("/portfolio")
public class PortfolioController {

    private ProfileService profileService;

    @GetMapping
    public String portfolio(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        model.addAttribute("user", userDetails.getUser().getProfile());
        return "portfolio";
    }

    @PostMapping("/deposits/save")
    public String addDeposit(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             Model model,
                             @ModelAttribute("depositDto") DepositRequestDto depositRequestDto,
                             @ModelAttribute("stockDto") StockRequestDto stockRequestDto,
                             BindingResult result) {
        return "portfolio";
    }

}
