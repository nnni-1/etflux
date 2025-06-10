package ru.alexeev.etflux.entity.assets;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deposits")
public class Deposit extends Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bankName;
    private String depositName;
    private BigDecimal interestRate;
    private LocalDate maturityDate;
    private LocalDate registrationDate;
    private LocalDateTime lastUpdate;
    private Boolean autoRenewable;
    private Integer termInDays;
    private BigDecimal sumFinal;
    private Double percentYear;
    private Boolean capitalization;
    private Integer capitalizationDays;
}
