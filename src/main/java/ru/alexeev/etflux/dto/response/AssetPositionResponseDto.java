package ru.alexeev.etflux.dto.response;

import lombok.*;
import lombok.experimental.Accessors;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dictionary.Currency;
import ru.alexeev.etflux.entity.AssetType;
import ru.alexeev.etflux.entity.assets.Asset;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class AssetPositionResponseDto<T extends Asset> {
    private AssetTypeName assetTypeName;
    private Long assetId;
    private String name;
    private BigDecimal quantity;
    private BigDecimal averageBuyPrice;
    private BigDecimal currentPricePerUnit;
    private LocalDateTime lastUpdated;
    private List<TransactionResponseDto> transactionHistory;
    private BigDecimal growthInCurrencyUnits;
    private BigDecimal growthInPercents;

    private T asset;

}
