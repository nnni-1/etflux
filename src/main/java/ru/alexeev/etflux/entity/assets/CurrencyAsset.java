package ru.alexeev.etflux.entity.assets;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import ru.alexeev.etflux.dictionary.Currency;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Entity
@Table(name = "currencies")
public class CurrencyAsset extends Asset {

    private Currency currency;
    private Long quantity;
}
