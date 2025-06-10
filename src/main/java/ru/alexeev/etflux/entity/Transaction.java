package ru.alexeev.etflux.entity;

import lombok.*;

import jakarta.persistence.*;
import ru.alexeev.etflux.dictionary.TransactionType;
import ru.alexeev.etflux.entity.assets.Asset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_profile_id")
    private UserProfile user;

    @ManyToOne
    private UserAssetPosition asset;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal quantity;
    private BigDecimal pricePerUnit;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\": %d, \"asset\": \"%s\", \"type\": %s, \"quantity\": %s, \"pricePerUnit\": %s}",
                id, asset, type, quantity, pricePerUnit
        );

    }

}
