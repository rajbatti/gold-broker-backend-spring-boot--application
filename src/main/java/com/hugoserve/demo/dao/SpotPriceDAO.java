package com.hugoserve.demo.dao;

import java.util.List;

import com.hugoserve.demo.proto.entity.MarketDataEntity;

public interface SpotPriceDAO {
    void spotPriceUpdate(MarketDataEntity marketDataEntity);

    MarketDataEntity spotPriceCurrent(String metal);

    List<MarketDataEntity> getSpotPriceList(String metal);


}
