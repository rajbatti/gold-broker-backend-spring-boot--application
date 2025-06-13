package com.hugoserve.demo.dao;

import java.util.Optional;

import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;

public interface WalletDao {
    void createWallet(UserEntity userEntity);

    void creditAmount(String walletID, Double amount);

    void debitAmount(String walletId, Double amount);

    WalletEntity balance(String walletId);

    String getWalletId(String username);

    Optional<WalletEntity> getWallet(String username);

    void createWalletTransaction(WalletTransactionEntity walletTransactionEntity);

    WalletTransactionEntity getWalletTransactionById(String walletTransactionId);

    void updateWalletTransaction(String WalletTransactionId, String status);
}
