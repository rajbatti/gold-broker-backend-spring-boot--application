package com.hugoserve.demo.facade;

import java.util.Optional;

import com.hugoserve.demo.dao.TransactionDao;
import com.hugoserve.demo.proto.entity.AssetEntity;
import com.hugoserve.demo.proto.entity.AssetTransactionEntity;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import org.springframework.stereotype.Component;

@Component
public class TransactionFacade {
    private final TransactionDao transactionDao;

    TransactionFacade(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public String createTransaction(UserEntity userEntity, AssetTransactionEntity assetTransactionEntity) {
        return transactionDao.createTransaction(userEntity, assetTransactionEntity);
    }

    public AssetTransactionEntity getTransactionByID(String transaction_id) {
        return transactionDao.getTransactionByID(transaction_id);
    }

    public Optional<AssetEntity> assetBalance(UserEntity userEntity, String metal) {
        return transactionDao.assetBalance(userEntity, metal);
    }

    public void creditAsset(AssetTransactionEntity assetTransactionEntity) {
        transactionDao.creditAsset(assetTransactionEntity);
    }

    public void debitAsset(AssetTransactionEntity assetTransactionEntity) {
        transactionDao.debitAsset(assetTransactionEntity);
    }

    public void debitAssetOnHold(AssetTransactionEntity assetTransactionEntity) {
        transactionDao.debitAssetOnHold(assetTransactionEntity);
    }

    public void updateTransaction(AssetTransactionEntity assetTransactionEntity, double price, WalletTransactionEntity walletTransactionEntity,
                                   String isSuccess) {
        transactionDao.updateTransaction(assetTransactionEntity, price, walletTransactionEntity, isSuccess);
    }

    public void updateTransactionStatus(AssetTransactionEntity assetTransactionEntity, String isSuccess) {
        transactionDao.updateTransactionStatus(assetTransactionEntity,isSuccess);
    }
}
