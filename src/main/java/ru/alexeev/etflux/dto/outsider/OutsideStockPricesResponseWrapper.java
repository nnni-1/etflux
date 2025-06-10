package ru.alexeev.etflux.dto.outsider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class OutsideStockPricesResponseWrapper {
    @JsonProperty("lastPrices")
    private List<LastStockPrice> outsideStockPrices;
}
