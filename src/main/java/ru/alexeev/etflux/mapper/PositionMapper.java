package ru.alexeev.etflux.mapper;

import ru.alexeev.etflux.dto.response.AssetPositionResponseDto;
import ru.alexeev.etflux.dto.response.TransactionResponseDto;
import ru.alexeev.etflux.entity.Transaction;
import ru.alexeev.etflux.entity.UserAssetPosition;
import ru.alexeev.etflux.entity.assets.Asset;

import java.util.List;


public final class PositionMapper {

//    public static ShareAssetPositionResponseDto toShareAssetPositionResponseDto(UserAssetPosition userAssetPosition) {
//        Asset asset = userAssetPosition.getAsset();
//        if (asset instanceof Stock stock){
//            if (stock.getType().equals("share")){
//                return ShareAssetPositionResponseDto.builder()
//                        .name(stock.getName())
//                        .uid(stock.getFigi())
//                        .figi(stock.getFigi())
//                        .isin(stock.getIsin())
//                        .source(stock.getSource())
//                        .ticker(stock.getTicker())
//                        .currency(stock.getCurrency())
//                        .build();
//            }
//        }
//        return null;
//    }

//    public static BondPositionResponseDto toBondPositionResponseDto(UserAssetPosition userAssetPosition) {
//        Asset asset = userAssetPosition.getAsset();
//        if (asset instanceof Stock stock){
//            if (stock.getType().equals("bond")){
//                return BondPositionResponseDto.builder()
//                        .isin(stock.getIsin())
//                        .uid(stock.getUid())
//                        .name(stock.getName())
//                        .currency(stock.getCurrency())
//                        .figi(stock.getFigi())
//                        .source(stock.getSource())
//                        .ticker(stock.getTicker())
//                        .build();
//            }
//        } return null;
//    }

    public static <T extends Asset> AssetPositionResponseDto<T> toAssetDto(UserAssetPosition userAssetPosition, T asset) {
        AssetPositionResponseDto<T> assetPositionResponseDto = new AssetPositionResponseDto<>();

        assetPositionResponseDto
                .setAsset(asset)
                .setAssetId(userAssetPosition.getId())
                .setQuantity(userAssetPosition.getQuantity())
                .setName(userAssetPosition.getAsset().getName())
                .setAssetTypeName(userAssetPosition.getAsset().getType())
                .setAverageBuyPrice(userAssetPosition.getAveragePrice())
                .setTransactionHistory(toTransactionResponseDto(userAssetPosition.getTransactions()))
                .setLastUpdated(userAssetPosition.getLastUpdated());

        assetPositionResponseDto.setAsset(asset);
        return assetPositionResponseDto;
    }

    public static List<TransactionResponseDto> toTransactionResponseDto(List<Transaction> transactions){
        if (transactions == null) return null;
        return transactions.stream().map(t -> TransactionResponseDto.builder()
                .id(t.getId())
                .type(t.getType())
                .pricePerUnit(t.getPricePerUnit())
                .createdAt(t.getCreatedAt())
                .quantity(t.getQuantity())
                .build()).toList();
    }

}
