package com.hugoserve.demo.service.Helper;


import com.hugoserve.demo.model.UserData;
import com.hugoserve.demo.proto.AssetTrasactionRequestDTO;
import com.hugoserve.demo.proto.DataDTO;
import com.hugoserve.demo.proto.PriceDTO;
import com.hugoserve.demo.proto.UserDetailsDTO;
import com.hugoserve.demo.proto.entity.AssetTransactionEntity;
import com.hugoserve.demo.proto.entity.HistoricPriceEntity;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.utils.DateTimeUtils;

public class DTOtoEntity {
    public static UserEntity userEntityCreate(UserDetailsDTO userDetailsDTO) {
        return UserEntity.newBuilder()
                .setUsername(userDetailsDTO.getUsername())
                .setPassword(userDetailsDTO.getPassword())
                .build();
    }

    public static UserEntity userEntityCreate(UserData userDetailsDTO) {
        return UserEntity.newBuilder()
                .setUsername(userDetailsDTO.getUsername())
                .setPassword(userDetailsDTO.getPassword())
                .build();
    }

    public static MarketDataEntity marketDataEntity(String metal, String Currency, PriceDTO priceDTO) {
        return MarketDataEntity.newBuilder().setMetal(metal)
                .setCurrency(Currency)
                .setDate(priceDTO.getDate())
                .setWeightUnit(priceDTO.getWeightUnit())
                .setBuyPrice(priceDTO.getAsk())
                .setSellPrice(priceDTO.getBid())
                .setValue(priceDTO.getValue())
                .setAveragePrice(priceDTO.getMid())
                .setPerformance(priceDTO.getPerformance())
                .build();

    }

    public static AssetTransactionEntity assetTransactionEntityCreate(AssetTrasactionRequestDTO assetTrasactionRequestDTO) {
        return AssetTransactionEntity.newBuilder()
                .setUsername(assetTrasactionRequestDTO.getUsername())
                .setStatus("PENDING")
                .setTransactionType(assetTrasactionRequestDTO.getType())
                .setMetal(assetTrasactionRequestDTO.getMetal())
                .setQuantity(assetTrasactionRequestDTO.getQuantity())
                .setPrice(assetTrasactionRequestDTO.getPrice())
                .setWalletTransactionId(assetTrasactionRequestDTO.getWalletTransactionId())
                .build();
    }

    public static HistoricPriceEntity historicPriceEntity(String metal, DataDTO dataDTO) {
        return HistoricPriceEntity.newBuilder()
                .setMetal(metal)
                .setOpen(dataDTO.getOpen())
                .setWeightUnit(dataDTO.getWeightUnit())
                .setLow(dataDTO.getLow())
                .setHigh(dataDTO.getHigh())
                .setDate(DateTimeUtils.convertToTimestamp(dataDTO.getDate()))
                .setClose(dataDTO.getClose())
                .build();
    }
}
