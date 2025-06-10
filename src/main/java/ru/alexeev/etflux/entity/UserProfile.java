package ru.alexeev.etflux.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.alexeev.etflux.dictionary.InvestStrategy;
import ru.alexeev.etflux.entity.assets.Asset;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "user_profile")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal balance;

    private BigDecimal lastYearGrow;

    private BigDecimal lastMonthGrow;

    private String avatarUri;

    @Enumerated(EnumType.STRING)
    private InvestStrategy investStrategy;

    @OneToMany(mappedBy = "userProfile", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAssetPosition> assetPositions;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    public <T extends Asset> List<UserAssetPosition> getAssetsByType(Class<T> type) {
        return assetPositions.stream()
                .filter(assetPosition -> type.equals(assetPosition.getAsset().getClass()))
                .toList();
    }

    public void addAsset(UserAssetPosition assetPosition){
        assetPositions.add(assetPosition);
    }

    @Override
    public String toString(){
        return String.format("{\"id\": \"%1$s\", \"user\": \"%2$s\", \"balance\": \"%3$s\", \"lastYearGrow\": \"%4$s\", \"lastMonthGrow\": \"%5$s\", \"transactions\": \"%6$s\"}",
                id, user, balance, lastYearGrow, lastMonthGrow, transactions);
    }
}
