package ru.alexeev.etflux.entity.assets;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dictionary.Currency;
import ru.alexeev.etflux.entity.AssetType;
import ru.alexeev.etflux.entity.UserAssetPosition;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "assets")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@MappedSuperclass
public abstract class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Transient
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private AssetTypeName type;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    private String name;

    @OneToMany
    private List<UserAssetPosition> children;

    @Override
    public String toString() {
        return String.format(
                """
                        {
                          "id": %d,
                          "type": "%s",
                          "currency": "%s",
                          "name": "%s",
                          "price": %s
                        }""",
                id,
                type != null ? type.name() : "null",
                currency != null ? currency.name() : "null",
                name != null ? name : "null",
                price != null ? price.stripTrailingZeros().toPlainString() : "null"
        );
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setType(AssetTypeName type) {
        this.type = type;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChildren(List<UserAssetPosition> children) {
        this.children = children;
    }
}
