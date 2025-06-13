package com.hugoserve.demo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.hugoserve.demo.Exception.DuplicateKeyException;
import com.hugoserve.demo.constants.DB_Constants;
import com.hugoserve.demo.constants.SqlQueryConstants;
import com.hugoserve.demo.dao.SpotPriceDAO;
import com.hugoserve.demo.dao.TransactionDao;
import com.hugoserve.demo.dao.WalletDao;
import com.hugoserve.demo.dao.helper.RdstoProtoMapper;
import com.hugoserve.demo.proto.entity.Asset;
import com.hugoserve.demo.proto.entity.AssetEntity;
import com.hugoserve.demo.proto.entity.AssetTransactionEntity;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import com.hugoserve.demo.provider.MySqlProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class TransactionDaoImpl implements TransactionDao {
    private final MySqlProvider mySqlProvider;
    private final Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);

    TransactionDaoImpl(MySqlProvider mySqlProvider) {
        this.mySqlProvider = mySqlProvider;
    }

    @Override
    public String createTransaction(UserEntity userEntity, AssetTransactionEntity assetTransactionEntity) {
        String transaction_id = UUID.randomUUID().toString();
        Map<String, Object> assetRequest = new HashMap<>();
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_TRANSACTION_ID, transaction_id);
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_USERNAME, userEntity.getUsername());
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_METAL, assetTransactionEntity.getMetal());
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_QUNANTITY, assetTransactionEntity.getQuantity());
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_PRICE, assetTransactionEntity.getPrice());
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_TYPE, assetTransactionEntity.getTransactionType());
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_WALLET_TRANSACTION_ID, assetTransactionEntity.getWalletTransactionId());
        assetRequest.put(DB_Constants.ASSET_TRANSACTION_STATUS, assetTransactionEntity.getStatus());
        return createTransactionEntry(assetRequest).getTransactionId();
    }

    @Override
    public AssetTransactionEntity getTransactionByID(String transaction_id) {
        Map<String, Object> transaction = new HashMap<>();
        transaction.put(DB_Constants.ASSET_TRANSACTION_TRANSACTION_ID, transaction_id);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_TRANSACTION_BY_ASSET_ID, transaction);
        return RdstoProtoMapper.deserializeAssetTransactionEntity(rows.getFirst());
    }

    @Override
    public Optional<AssetEntity> assetBalance(UserEntity userEntity, String metal) {
        Map<String, Object> user = new HashMap<>();
        user.put(DB_Constants.ASSET_USERNAME, userEntity.getUsername());
        user.put(DB_Constants.ASSET_METAL, metal);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_ASSET_USERNAME_METAL, user);
        if (rows.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(RdstoProtoMapper.deserializeAssetEntity(rows.getFirst()));
    }

    public AssetTransactionEntity createTransactionEntry(Map<String, Object> assetRequest) {
        try {
            mySqlProvider.create(SqlQueryConstants.INSERT_ASSET_TRANSACTION, assetRequest);
        } catch (DuplicateKeyException e) {
            logger.info("Transaction already exist now executing");
        }
        return this.getTransactionByID((String) assetRequest.get(DB_Constants.ASSET_TRANSACTION_TRANSACTION_ID));
    }

    @Override
    public void creditAsset(AssetTransactionEntity assetTransactionEntity) {
        Map<String, Object> assetRequest = new HashMap<>();
        assetRequest.put(DB_Constants.ASSET_USERNAME, assetTransactionEntity.getUsername());
        assetRequest.put(DB_Constants.ASSET_METAL, assetTransactionEntity.getMetal());
        assetRequest.put(DB_Constants.ASSET_QUNATITY, assetTransactionEntity.getQuantity());
        mySqlProvider.update(SqlQueryConstants.CREDIT_ASSET, assetRequest);
    }

    @Override
    public void debitAsset(AssetTransactionEntity assetTransactionEntity) {
        Map<String, Object> assetRequest = new HashMap<>();
        assetRequest.put(DB_Constants.ASSET_USERNAME, assetTransactionEntity.getUsername());
        assetRequest.put(DB_Constants.ASSET_METAL, assetTransactionEntity.getMetal());
        assetRequest.put(DB_Constants.ASSET_QUNATITY, assetTransactionEntity.getQuantity());
        mySqlProvider.update(SqlQueryConstants.HOLD_ASSET_BALANCE, assetRequest);
    }

    @Override
    public void debitAssetOnHold(AssetTransactionEntity assetTransactionEntity) {
        Map<String, Object> assetRequest = new HashMap<>();
        assetRequest.put(DB_Constants.ASSET_USERNAME, assetTransactionEntity.getUsername());
        assetRequest.put(DB_Constants.ASSET_METAL, assetTransactionEntity.getMetal());
        assetRequest.put(DB_Constants.ASSET_QUNATITY, assetTransactionEntity.getQuantity());
        mySqlProvider.update(SqlQueryConstants.SUBTRACT_HOLD_ASSET_BALANCE, assetRequest);
    }

    @Override
    public void updateTransaction(AssetTransactionEntity assetTransactionEntity, double price, WalletTransactionEntity walletTransactionEntity,
                                  String isSuccess) {
        Map<String, Object> transactionUpdate = new HashMap<>();
        transactionUpdate.put(DB_Constants.ASSET_TRANSACTION_TRANSACTION_ID, assetTransactionEntity.getTransactionId());
        transactionUpdate.put(DB_Constants.ASSET_TRANSACTION_STATUS, isSuccess);
        transactionUpdate.put(DB_Constants.ASSET_TRANSACTION_WALLET_TRANSACTION_ID, walletTransactionEntity.getTransactionId());
        transactionUpdate.put(DB_Constants.ASSET_TRANSACTION_PRICE, price);
        mySqlProvider.update(SqlQueryConstants.UPDATE_TRANSACTION, transactionUpdate);
    }

    @Override
    public void updateTransactionStatus(AssetTransactionEntity assetTransactionEntity,String isSuccess) {
        Map<String, Object> transactionUpdate = new HashMap<>();
        transactionUpdate.put(DB_Constants.ASSET_TRANSACTION_TRANSACTION_ID, assetTransactionEntity.getTransactionId());
        transactionUpdate.put(DB_Constants.ASSET_TRANSACTION_STATUS, isSuccess);
        mySqlProvider.update(SqlQueryConstants.UPDATE_TRANSACTION_STATUS, transactionUpdate);
    }


}
