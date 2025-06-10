package ru.alexeev.etflux.dto.outsider;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@Builder
@Setter
@Getter
@NoArgsConstructor
public class StockPrice {
    @JsonProperty("units")
    private String units;

    @JsonProperty("nano")
    private long nano;
}
