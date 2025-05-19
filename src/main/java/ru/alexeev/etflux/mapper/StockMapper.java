package ru.alexeev.etflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.alexeev.etflux.dto.outsider.OutsideStockResponse;
import ru.alexeev.etflux.entity.Stock;

@Mapper(componentModel = "spring")
public interface StockMapper {

    @Mapping(source = "ticker", target = "ticker")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "instrumentType", target = "type")
    @Mapping(source = "figi", target = "figi")
    @Mapping(source = "uid", target = "uid")
    @Mapping(source = "isin", target = "isin")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "source", constant = "TINKOFF")
    Stock outsideToStock(OutsideStockResponse response);
}
