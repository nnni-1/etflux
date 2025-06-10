package ru.alexeev.etflux.dto.request;

import lombok.*;
import ru.alexeev.etflux.dictionary.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class StockRequestDto {
    private TransactionType transactionType;
    private String ticker;
    private String figi;
    private LocalDateTime timeOfBuy;
    private LocalDateTime timeOfSell;
    private BigDecimal buyPrice;
    private BigDecimal quantity;
    private String type;
}

