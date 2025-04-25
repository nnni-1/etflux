package ru.alexeev.etflux.entity;

import lombok.*;
import ru.alexeev.etflux.dictionary.Currency;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "asset_type_id")
    private AssetType assetType;

    @Column(nullable = true, unique = true, length = 15)
    private String ticker;

    @Column(name = "average_price")
    private BigDecimal averagePrice;

    private BigDecimal quantity;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    private Currency currency;
}
