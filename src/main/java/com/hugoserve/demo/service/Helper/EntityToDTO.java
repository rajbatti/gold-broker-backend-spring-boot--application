package com.hugoserve.demo.service.Helper;

import com.hugoserve.demo.proto.MetalDataDTO;
import com.hugoserve.demo.proto.entity.AssetTransactionEntity;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import com.hugoserve.demo.proto.response.AssetTrasactionResponseDTO;
import com.hugoserve.demo.proto.response.WalletResponseDTO;
import com.hugoserve.demo.proto.response.WalletTrasactionResponseDTO;


public class EntityToDTO {

    public static MetalDataDTO serializeToMetalDataDTO(MarketDataEntity marketDataEntity) {
        return MetalDataDTO.newBuilder()
                .setMetal(marketDataEntity.getMetal())
                .setCurrency(marketDataEntity.getCurrency())
                .setDate(marketDataEntity.getDate())
                .setWeightUnit(marketDataEntity.getWeightUnit())
                .setBuyPrice(marketDataEntity.getBuyPrice())
                .setSellPrice(marketDataEntity.getSellPrice())
                .build();
    }

    public static WalletResponseDTO serializetoWalletResponseDTO(WalletEntity walletEntity) {
        return WalletResponseDTO.newBuilder()
                .setBalance(walletEntity.getBalance())
                .setStatus(walletEntity.getStatus())
                .build();
    }

    public static WalletTrasactionResponseDTO serializetoWalletResponseDTO(WalletTransactionEntity walletTransactionEntity,
                                                                           WalletEntity walletEntity) {
        return WalletTrasactionResponseDTO.newBuilder()
                .setBalance(walletEntity.getBalance())
                .setStatus(walletTransactionEntity.getStatus())
                .setCreatedAt(walletTransactionEntity.getCreatedAt())
                .setType(walletTransactionEntity.getTransactionType())
                .build();

    }

    public static AssetTrasactionResponseDTO serializetoAssetTransactionResponseDTO(AssetTransactionEntity assetTransactionEntity) {
        return AssetTrasactionResponseDTO.newBuilder()
                .setMetal(assetTransactionEntity.getMetal())
                .setQuantity(assetTransactionEntity.getQuantity())
                .setType(assetTransactionEntity.getTransactionType())
                .setCreatedAt(assetTransactionEntity.getCreatedAt())
                .setStatus(assetTransactionEntity.getStatus())
                .setPrice(assetTransactionEntity.getPrice())
                .build();
    }

}
