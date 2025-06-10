package ru.alexeev.etflux.dto.outsider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class LastStockPrice {
    @JsonProperty("figi")
    private String figi;

    @JsonProperty("price")
    private StockPrice price;

    @JsonProperty("time")
    private String time;

    @JsonProperty("instrumentUid")
    private String instrumentUid;

    @JsonProperty("lastPriceType")
    private String lastPriceType;
}
