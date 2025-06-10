package ru.alexeev.etflux.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.alexeev.etflux.dto.request.DepositRequestDto;
import ru.alexeev.etflux.dto.response.DepositDtoResponse;
import ru.alexeev.etflux.entity.assets.Deposit;

@Mapper(componentModel = "spring")
public interface DepositMapper {

    @Mapping(source = "capitalization", target = "capitalization")
    @Mapping(source = "sumFinal", target = "price")
    Deposit toDeposit(DepositRequestDto depositDtoResponse);

    DepositDtoResponse toResponse(Deposit deposit);

    DepositDtoResponse requestToResponce(DepositRequestDto deposit);
}
