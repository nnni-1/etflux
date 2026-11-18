package ru.alexeev.etflux.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "stocks")
public class Stock {
    @Id
    private Long id;
    private String ticker;
    private String name;
    private String type;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    private String source;
    private String figi;
    private String uid;
    private String isin;
}
