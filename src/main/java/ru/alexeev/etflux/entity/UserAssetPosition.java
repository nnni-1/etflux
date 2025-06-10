package ru.alexeev.etflux.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.entity.assets.Asset;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "asset_positions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
//текущее состояние позиции портфеля
public class UserAssetPosition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Asset asset;

    private BigDecimal quantity;
    private BigDecimal averagePrice;
    private BigDecimal totalInvestedAmount;
    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private boolean updated;
    private Boolean tookInShortFlag;

    @OneToMany
    private List<Transaction> transactions;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @PreUpdate
    public void preUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public void addUnits(BigDecimal newQuantity, BigDecimal pricePerUnit, boolean isSell) {
        if (this.quantity == null || this.quantity.compareTo(BigDecimal.ZERO) <= 0) {
            this.quantity = newQuantity;
            this.averagePrice = pricePerUnit;
        } else {
            BigDecimal totalCurrentInvestment = this.quantity.multiply(this.averagePrice);
            BigDecimal totalNewInvestment = newQuantity.multiply(pricePerUnit);
            if (isSell) {
                this.quantity = this.quantity.subtract(newQuantity);
                if (this.quantity.compareTo(BigDecimal.ZERO) <= 0) {
                    tookInShortFlag = true;
                }
            } else {
                this.quantity = this.quantity.add(newQuantity);
            }
            this.averagePrice = totalCurrentInvestment.add(totalNewInvestment).divide(this.quantity, RoundingMode.HALF_UP);
        }
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean isForAsset(Asset asset) {
        return this.asset.getId().equals(asset.getId());
    }

    @Override
    public String toString() {
        return String.format(
                "{\"id\": %d, \"asset\": %s, \"quantity\": %s, \"averagePrice\": %s, \"totalInvestedAmount\": %s, \"lastUpdated\": \"%s\", \"createdAt\": \"%s\", \"updated\": %b}",
                id,
                asset != null ? asset.toString() : "null",
                quantity != null ? quantity.stripTrailingZeros().toPlainString() : "null",
                averagePrice != null ? averagePrice.stripTrailingZeros().toPlainString() : "null",
                totalInvestedAmount != null ? totalInvestedAmount.stripTrailingZeros().toPlainString() : "null",
                lastUpdated,
                createdAt,
                updated);
    }
}
