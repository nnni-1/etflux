package ru.alexeev.etflux.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DepositDtoResponse {
    private String bankName;
    private BigDecimal interestRate;
    private LocalDate maturityDate;
    private LocalDateTime registrationDate;
    private Boolean autoRenewable;
    private Integer termInDays;
    private BigDecimal sumFinal;
    private Double percentYear;
}
