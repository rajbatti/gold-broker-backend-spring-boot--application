package com.hugoserve.demo.facade;

import java.util.Optional;

import com.hugoserve.demo.dao.WalletDao;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.endpoints.internal.Value;

@Component
public class WalletFacade {
    private final WalletDao walletDao;

    WalletFacade(WalletDao walletDao) {
        this.walletDao = walletDao;
    }

    public void createWallet(UserEntity userEntity) {
        walletDao.createWallet(userEntity);
    }

    public void creditAmount(String walletId, Double amount) {
        walletDao.creditAmount(walletId, amount);
    }

    public void debitAmount(String walletId, Double amount) {
        walletDao.debitAmount(walletId, amount);
    }

    public WalletEntity balance(String walletID) {
        return walletDao.balance(walletID);
    }

    public String getWalletId(String username) {
        return walletDao.getWalletId(username);
    }

    public Optional<WalletEntity> getWallet(String username) {
        return walletDao.getWallet(username);
    }

    public void createTransaction(WalletTransactionEntity walletTransactionEntity) {
        walletDao.createWalletTransaction(walletTransactionEntity);
    }

    public WalletTransactionEntity getWalletTransactionById(String walletTransactionId) {
        return walletDao.getWalletTransactionById(walletTransactionId);
    }

    public void updateWalletTransaction(String walletTransactionId, String status) {
        walletDao.updateWalletTransaction(walletTransactionId, status);
    }

}
