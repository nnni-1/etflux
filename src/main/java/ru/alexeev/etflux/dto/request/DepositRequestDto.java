package ru.alexeev.etflux.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DepositRequestDto {

    private String bankName;

    private BigDecimal interestRate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate maturityDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private LocalDate registrationDate;

    private Boolean autoRenewable;

    private Integer termInDays;

    private BigDecimal sumFinal;

    private Double percentYear;

    private Boolean capitalization;

    private Integer capitalizationDays;

}
