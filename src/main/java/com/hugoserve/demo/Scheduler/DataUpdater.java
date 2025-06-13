package com.hugoserve.demo.Scheduler;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.Timestamp;
import com.hugoserve.demo.dao.SpotPriceDAO;
import com.hugoserve.demo.proto.MetalPriceDTO;
import com.hugoserve.demo.proto.PriceDTO;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import com.hugoserve.demo.service.Helper.DTOtoEntity;
import com.hugoserve.demo.utils.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class DataUpdater {
    @Value("${api.marketdata.url}")
    private String marketURL;
    private final SpotPriceDAO spotPriceDAO;
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(DataUpdater.class);

    public DataUpdater(RestTemplate restTemplate, SpotPriceDAO spotPriceDAO) {
        this.restTemplate = restTemplate;
        this.spotPriceDAO = spotPriceDAO;
    }

    @Scheduled(fixedRate = 4000 * 60)
    public void marketDataUpdate() {
        MetalPriceDTO metalPriceDTO = null;
        try {
            metalPriceDTO = restTemplate.getForObject(marketURL, MetalPriceDTO.class);
        } catch (HttpClientErrorException e) {
            logger.error("Error in MarketApi fetching :", e);
        }

        if (metalPriceDTO == null || metalPriceDTO.equals(MetalPriceDTO.getDefaultInstance())) {
            logger.info("No data in Api");
            return;
        }

        MarketDataEntity currentPriceEntity = spotPriceDAO.spotPriceCurrent(metalPriceDTO.getMetal());

        if (currentPriceEntity.equals(MarketDataEntity.getDefaultInstance())) {
            logger.info("Database is Empty");
            for (PriceDTO priceDTO : metalPriceDTO.getEmbedded().getItemsList()) {
                MarketDataEntity marketDataEntity = DTOtoEntity.marketDataEntity(metalPriceDTO.getMetal(), metalPriceDTO.getCurrency(), priceDTO);
                spotPriceDAO.spotPriceUpdate(marketDataEntity);
                logger.info("All the Data got Addded");
            }
        } else {
            List<PriceDTO> metalPriceList = new ArrayList<>(metalPriceDTO.getEmbedded().getItemsList());
            metalPriceList.reversed();
            for (PriceDTO priceDTO : metalPriceList) {
                Timestamp dateTimeStr1 = currentPriceEntity.getDate();
                Timestamp dateTimeStr2 = priceDTO.getDate();
                int isAfter = DateTimeUtils.compareTimestamps(dateTimeStr1, dateTimeStr2);
                if (isAfter < 0) {
                    MarketDataEntity marketDataEntity = DTOtoEntity.marketDataEntity(metalPriceDTO.getMetal(), metalPriceDTO.getCurrency(), priceDTO);
                    spotPriceDAO.spotPriceUpdate(marketDataEntity);
                    logger.info("A new data got added");
                } else {
                    break;
                }
            }
        }
    }


}
