package com.hugoserve.demo.facade;

import java.util.List;

import com.hugoserve.demo.dao.SpotPriceDAO;
import com.hugoserve.demo.proto.entity.HistoricPriceEntity;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import org.springframework.stereotype.Component;

@Component
public class SpotPricefacade {
    private final SpotPriceDAO spotPriceDAO;

    SpotPricefacade(SpotPriceDAO spotPriceDAO) {
        this.spotPriceDAO = spotPriceDAO;
    }

    public void spotPriceUpdate(MarketDataEntity marketDataEntity) {
        spotPriceDAO.spotPriceUpdate(marketDataEntity);
    }

    public MarketDataEntity spotPriceCurrent(String metal) {
        return spotPriceDAO.spotPriceCurrent(metal);
    }

    public List<MarketDataEntity> getSpotPriceList(String metal) {
        return spotPriceDAO.getSpotPriceList(metal);
    }


}
