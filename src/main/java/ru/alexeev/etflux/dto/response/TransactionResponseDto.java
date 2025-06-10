package ru.alexeev.etflux.dto.response;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.dictionary.TransactionType;
import ru.alexeev.etflux.entity.UserProfile;
import ru.alexeev.etflux.entity.assets.Asset;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
public class TransactionResponseDto {
    private Long id;
    private TransactionType type;
    private BigDecimal quantity;
    private BigDecimal pricePerUnit;
    private LocalDateTime createdAt;
}
