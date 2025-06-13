package com.hugoserve.demo.dao.helper;

import java.sql.Timestamp;
import java.util.Map;

import com.hugoserve.demo.constants.DB_Constants;
import com.hugoserve.demo.proto.entity.AssetEntity;
import com.hugoserve.demo.proto.entity.AssetTransactionEntity;
import com.hugoserve.demo.proto.entity.HistoricPriceEntity;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import com.hugoserve.demo.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdstoProtoMapper {

    private RdstoProtoMapper() {
    }

    private static final Logger logger = LoggerFactory.getLogger(RdstoProtoMapper.class);

    public static UserEntity deserializetoUserEntity(Map<String, Object> result) {
        try {
            return UserEntity.newBuilder()
                    .setUsername((String) result.get(DB_Constants.USERS_USERNAME))
                    .setPassword((String) result.get(DB_Constants.USERS_PASSWORD))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return UserEntity.getDefaultInstance();
        }
    }

    public static MarketDataEntity deserializetoMarketDataEntity(Map<String, Object> result) {
        try {
            return MarketDataEntity.newBuilder()
                    .setMetal((String) result.get(DB_Constants.MARKET_DATA_METAL))
                    .setCurrency((String) result.get(DB_Constants.MARKET_DATA_CURRENCY))
                    .setDate(DateTimeUtils.instantToGoogleTimestamp(((Timestamp) result.get(DB_Constants.MARKET_DATA_DATE)).toInstant()))
                    .setWeightUnit((String) result.get(DB_Constants.MARKET_DATA_WEIGHT_UNIT))
                    .setBuyPrice((Double) result.get(DB_Constants.MARKET_BUY_PRICE))
                    .setSellPrice((Double) result.get(DB_Constants.MARKET_DATA_SELL_PRICE))
                    .setAveragePrice((Double) result.get(DB_Constants.MARKET_DATA_AVERAGE_PRICE))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return MarketDataEntity.getDefaultInstance();
        }
    }

    public static WalletEntity deseriliazeWalletEntity(Map<String, Object> result) {
        try {
            return WalletEntity.newBuilder()
                    .setWalletId((String) result.get(DB_Constants.WALLET_WALLET_ID))
                    .setBalance((Double) result.get(DB_Constants.WALLET_BALANCE))
                    .setStatus((String) result.get(DB_Constants.WALLET_STATUS))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return WalletEntity.getDefaultInstance();
        }
    }

    public static WalletTransactionEntity deserializeWalletTransactionEntity(Map<String, Object> result) {
        try {
            return WalletTransactionEntity.newBuilder()
                    .setTransactionId((String) result.get(DB_Constants.WALLET_TRANSACTION_TRANSACTION_ID))
                    .setWalletId((String) result.get(DB_Constants.WALLET_TRANSACTION_WALLET_ID))
                    .setCreatedAt(((Timestamp) result.get(DB_Constants.WALLET_TRANSACTION_CREATED_AT)).toString())
                    .setTransactionType((String) result.get(DB_Constants.WALLET_TRANSACTION_TYPE))
                    .setStatus((String) result.get(DB_Constants.WALLET_TRANSACTION_STATUS))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return WalletTransactionEntity.getDefaultInstance();
        }
    }

    public static AssetTransactionEntity deserializeAssetTransactionEntity(Map<String, Object> result) {
        try {
            return AssetTransactionEntity.newBuilder()
                    .setUsername((String) result.get(DB_Constants.ASSET_TRANSACTION_USERNAME))
                    .setTransactionId((String) result.get(DB_Constants.ASSET_TRANSACTION_TRANSACTION_ID))
                    .setQuantity((Double) result.get(DB_Constants.ASSET_TRANSACTION_QUNANTITY))
                    .setCreatedAt(((Timestamp) result.get(DB_Constants.ASSET_TRANSACTION_CREATED_AT)).toString())
                    .setMetal((String) result.get(DB_Constants.ASSET_TRANSACTION_METAL))
                    .setPrice((Double) result.get(DB_Constants.ASSET_TRANSACTION_PRICE))
                    .setTransactionType((String) result.get(DB_Constants.ASSET_TRANSACTION_TYPE))
                    .setStatus((String) result.get(DB_Constants.ASSET_TRANSACTION_STATUS))
                    .setWalletTransactionId((String) result.get(DB_Constants.ASSET_TRANSACTION_WALLET_TRANSACTION_ID))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return AssetTransactionEntity.getDefaultInstance();
        }

    }

    public static AssetEntity deserializeAssetEntity(Map<String, Object> result) {
        try {
            return AssetEntity.newBuilder()
                    .setMetal((String) result.get(DB_Constants.ASSET_METAL))
                    .setBalanceG((Double) result.get(DB_Constants.ASSET_BALANCE_GRAM))
                    .setUsername((String) result.get(DB_Constants.ASSET_USERNAME))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return AssetEntity.getDefaultInstance();
        }
    }

}
