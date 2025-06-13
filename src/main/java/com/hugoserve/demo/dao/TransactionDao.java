package com.hugoserve.demo.dao;

import java.util.Optional;

import com.hugoserve.demo.proto.entity.AssetEntity;
import com.hugoserve.demo.proto.entity.AssetTransactionEntity;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;

public interface TransactionDao {
    String createTransaction(UserEntity userEntity, AssetTransactionEntity assetTransactionEntity);

    AssetTransactionEntity getTransactionByID(String transaction_id);

    Optional<AssetEntity> assetBalance(UserEntity userEntity, String metal);


    void creditAsset(AssetTransactionEntity assetTransactionEntity);

    void debitAsset(AssetTransactionEntity assetTransactionEntity);

    void debitAssetOnHold(AssetTransactionEntity assetTransactionEntity);

    void updateTransaction(AssetTransactionEntity assetTransactionEntity, double price, WalletTransactionEntity walletTransactionEntity,
                           String isSuccess);

    void updateTransactionStatus(AssetTransactionEntity assetTransactionEntity, String isSuccess);


}
