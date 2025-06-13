package com.hugoserve.demo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.hugoserve.demo.Exception.DuplicateKeyException;
import com.hugoserve.demo.Exception.WalletException;
import com.hugoserve.demo.constants.DB_Constants;
import com.hugoserve.demo.constants.SqlQueryConstants;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.dao.WalletDao;
import com.hugoserve.demo.dao.helper.RdstoProtoMapper;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import com.hugoserve.demo.provider.impl.MySqlProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class WalletDaoImpl implements WalletDao {
    private final MySqlProviderImpl mySqlProvider;
    private final Logger logger = LoggerFactory.getLogger(WalletDaoImpl.class);

    WalletDaoImpl(MySqlProviderImpl mySqlProvider) {
        this.mySqlProvider = mySqlProvider;
    }

    @Override
    public void createWallet(UserEntity userEntity) {
        Map<String, Object> user = new HashMap<>();
        user.put(DB_Constants.WALLET_USERNAME, userEntity.getUsername());
        user.put(DB_Constants.WALLET_WALLET_ID, UUID.randomUUID().toString());
        user.put(DB_Constants.WALLET_STATUS, "ACTIVE");
        try {
            mySqlProvider.create(SqlQueryConstants.INSERT_WALLET, user);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage());
            createWallet(userEntity);
        }
    }

    @Override
    public void creditAmount(String walletId, Double amount) {
        Map<String, Object> walletmap = new HashMap<>();
        walletmap.put(DB_Constants.WALLET_WALLET_ID, walletId);
        walletmap.put(DB_Constants.WALLET_TRANSACTION_AMOUNT, amount);
        mySqlProvider.update(SqlQueryConstants.ADD_WALLET_BALANCE, walletmap);
    }


    @Override
    public void debitAmount(String walletId, Double amount) {
        Map<String, Object> user = new HashMap<>();
        user.put(DB_Constants.WALLET_WALLET_ID, walletId);
        user.put(DB_Constants.WALLET_TRANSACTION_AMOUNT, amount);
        mySqlProvider.update(SqlQueryConstants.SUBTRACT_WALLET_BALANCE, user);
    }

    @Override
    public WalletEntity balance(String walletID) {
        logger.info(walletID);
        Map<String, Object> walletmap = new HashMap<>();
        walletmap.put(DB_Constants.WALLET_WALLET_ID, walletID);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_WALLET_BY_WALLETID, walletmap);
        return RdstoProtoMapper.deseriliazeWalletEntity(rows.getFirst());

    }

    @Override
    public String getWalletId(String username) {
        Map<String, Object> user = new HashMap<>();
        user.put(DB_Constants.USERS_USERNAME, username);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_WALLET_BY_USERNAME, user);
        if (rows.isEmpty()) {
            throw new WalletException(StatusCodes.ERROR_WALLET_NOT_FOUND);
        }
        return (String) rows.getFirst().get(DB_Constants.WALLET_WALLET_ID);
    }

    @Override
    public Optional<WalletEntity> getWallet(String username) {
        Map<String, Object> user = new HashMap<>();
        user.put(DB_Constants.USERS_USERNAME, username);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_WALLET_BY_USERNAME, user);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(RdstoProtoMapper.deseriliazeWalletEntity(rows.getFirst()));
    }

    @Override
    public void createWalletTransaction(WalletTransactionEntity walletTransactionEntity) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put(DB_Constants.WALLET_TRANSACTION_TRANSACTION_ID, walletTransactionEntity.getTransactionId());
        transaction.put(DB_Constants.WALLET_WALLET_ID, walletTransactionEntity.getWalletId());
        transaction.put(DB_Constants.WALLET_TRANSACTION_AMOUNT, walletTransactionEntity.getAmount());
        transaction.put(DB_Constants.WALLET_TRANSACTION_TYPE, walletTransactionEntity.getTransactionType());
        transaction.put(DB_Constants.WALLET_TRANSACTION_STATUS, walletTransactionEntity.getStatus());
        try {
            mySqlProvider.create(SqlQueryConstants.INSERT_WALLET_TRANSACTION, transaction);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage());
            this.createWalletTransaction(walletTransactionEntity);
        }
    }

    @Override
    public WalletTransactionEntity getWalletTransactionById(String walletTransactionId) {
        Map<String, Object> transactionmap = new HashMap<>();
        transactionmap.put(DB_Constants.WALLET_TRANSACTION_TRANSACTION_ID, walletTransactionId);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_WALLET_TRANSACTION, transactionmap);
        return RdstoProtoMapper.deserializeWalletTransactionEntity(rows.getFirst());
    }

    @Override
    public void updateWalletTransaction(String walletTransactionId, String status) {
        Map<String, Object> transactionmap = new HashMap<>();
        transactionmap.put(DB_Constants.WALLET_TRANSACTION_TRANSACTION_ID, walletTransactionId);
        transactionmap.put(DB_Constants.WALLET_TRANSACTION_STATUS,status);
        mySqlProvider.update(SqlQueryConstants.UPDATE_WALLET_TRANSACTION_STATUS,transactionmap);
    }

}
