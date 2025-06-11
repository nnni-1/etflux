package ru.alexeev.etflux.web.controller.rest;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dto.request.DepositRequestDto;
import ru.alexeev.etflux.dto.request.StockRequestDto;
import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.entity.assets.Deposit;
import ru.alexeev.etflux.entity.assets.Stock;
import ru.alexeev.etflux.security.details.UserDetailsImpl;
import ru.alexeev.etflux.service.ProfileService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/portfolio")
@AllArgsConstructor
public class PortfolioRestController {

    private final ProfileService profileService;

    @PostMapping("/stock/save")
    public AssetPositionResponseDto<Stock> saveStock(@RequestBody StockRequestDto stockRequest,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails){

        return profileService.addStock(stockRequest, userDetails.getUser());
    }

    @PostMapping("/deposit/save")
    public AssetPositionResponseDto<Deposit> saveDeposit(@RequestBody DepositRequestDto depositRequest,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails){
        return profileService.addDeposit(depositRequest, userDetails.getUser());
    }

    @GetMapping
    public Map<String, List<AssetPositionResponseDto<?>>> getStock(@AuthenticationPrincipal UserDetailsImpl userDetails){
        System.out.println(" IN CONTROLLER " + userDetails.getUser().getUsername());
        return profileService.getAssets(userDetails.getUser());
    }

    @GetMapping("/total")
    public BigDecimal getTotal(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return profileService.getTotal(userDetails.getUser());
    }
}
