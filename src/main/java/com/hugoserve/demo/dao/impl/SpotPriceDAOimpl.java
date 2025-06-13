package com.hugoserve.demo.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hugoserve.demo.Exception.DuplicateKeyException;
import com.hugoserve.demo.constants.DB_Constants;
import com.hugoserve.demo.constants.SqlQueryConstants;
import com.hugoserve.demo.dao.SpotPriceDAO;
import com.hugoserve.demo.dao.helper.RdstoProtoMapper;
import com.hugoserve.demo.proto.entity.HistoricPriceEntity;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import com.hugoserve.demo.provider.MySqlProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class SpotPriceDAOimpl implements SpotPriceDAO {
    private final Logger logger = LoggerFactory.getLogger(UserDAOimpl.class);
    private MySqlProvider mySqlProvider;

    SpotPriceDAOimpl(MySqlProvider mySqlProvider) {
        this.mySqlProvider = mySqlProvider;
    }


    @Override
    public void spotPriceUpdate(MarketDataEntity marketDataEntity) {
        Map<String, Object> marketData = new HashMap<>();
        marketData.put(DB_Constants.MARKET_DATA_METAL, marketDataEntity.getMetal());
        marketData.put(DB_Constants.MARKET_DATA_CURRENCY, marketDataEntity.getCurrency());
        marketData.put(DB_Constants.MARKET_DATA_DATE, marketDataEntity.getDate());
        marketData.put(DB_Constants.MARKET_DATA_WEIGHT_UNIT, marketDataEntity.getWeightUnit());
        marketData.put(DB_Constants.MARKET_BUY_PRICE, marketDataEntity.getBuyPrice());
        marketData.put(DB_Constants.MARKET_DATA_AVERAGE_PRICE, marketDataEntity.getAveragePrice());
        marketData.put(DB_Constants.MARKET_DATA_SELL_PRICE, marketDataEntity.getSellPrice());
        marketData.put(DB_Constants.MARKET_DATA_VALUE, marketDataEntity.getValue());
        marketData.put(DB_Constants.MARKET_DATA_PERFORMANCE, marketDataEntity.getPerformance());
        try {
            mySqlProvider.create(SqlQueryConstants.INSERT_MARKET_DATA, marketData);
        } catch (DuplicateKeyException e) {
            logger.error("Data Already Exists", e);
        }
    }

    @Override
    public MarketDataEntity spotPriceCurrent(String metal) {
        Map<String, Object> metalData = new HashMap<>();
        metalData.put(DB_Constants.MARKET_DATA_METAL, metal);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_LATEST_MARKET_DATA_BY_METAL, metalData);
        if (rows.isEmpty()) {
            return MarketDataEntity.getDefaultInstance();
        }
        return RdstoProtoMapper.deserializetoMarketDataEntity(rows.getFirst());
    }

    @Override
    public List<MarketDataEntity> getSpotPriceList(String metal) {
        Map<String, Object> metalData = new HashMap<>();
        metalData.put(DB_Constants.MARKET_DATA_METAL, metal);
        List<Map<String, Object>> rows = mySqlProvider.query(SqlQueryConstants.SELECT_MARKET_DATA_BY_METAL, metalData);
        return rows.stream().map(row -> RdstoProtoMapper.deserializetoMarketDataEntity(row)).toList();

    }


}
