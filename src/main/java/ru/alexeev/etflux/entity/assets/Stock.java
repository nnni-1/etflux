package ru.alexeev.etflux.entity.assets;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.dictionary.Currency;
import ru.alexeev.etflux.dto.outsider.LastStockPrice;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "stocks")
public class Stock extends Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ticker;
    private String name;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private String source;
    private String figi;
    private String uid;
    private String isin;
    private String stockType;
    @Transient
    private LastStockPrice stockPrice;
}
