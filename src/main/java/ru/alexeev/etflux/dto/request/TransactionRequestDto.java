package ru.alexeev.etflux.dto.request;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TransactionRequestDto {
    private String transactionType;
    private BigDecimal quantity;
    private BigDecimal price;

}
