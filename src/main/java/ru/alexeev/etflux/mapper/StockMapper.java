package ru.alexeev.etflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.alexeev.etflux.dictionary.AssetTypeName;
import ru.alexeev.etflux.dto.outsider.OutsideStockResponse;
import ru.alexeev.etflux.dto.response.StockResponseDto;
import ru.alexeev.etflux.entity.assets.Stock;

import java.util.Arrays;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "ticker", target = "ticker")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "instrumentType", target = "stockType")
    @Mapping(source = "figi", target = "figi")
    @Mapping(source = "uid", target = "uid")
    @Mapping(source = "isin", target = "isin")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "source", constant = "TINKOFF")
    Stock outsideToStock(OutsideStockResponse response);

    default AssetTypeName map(String value) {
        if (value == null) return null;
        Stock stock = new Stock();
        return Arrays.stream(AssetTypeName.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown asset type: " + value));
    }

    default String map(AssetTypeName value) {
        return value != null ? value.name() : null;
    }
}
