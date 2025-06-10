package ru.alexeev.etflux.dto.outsider;

import lombok.*;

import java.util.List;
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class OutsideStockResponseWrapper {
    private List<OutsideStockResponse> instruments;
}
