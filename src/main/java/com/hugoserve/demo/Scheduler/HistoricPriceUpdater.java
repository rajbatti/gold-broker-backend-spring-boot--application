//
//package com.hugoserve.demo.Scheduler;
//
//import java.sql.SQLException;
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import com.google.protobuf.Timestamp;
//import com.hugoserve.demo.dao.SpotPriceDAO;
//import com.hugoserve.demo.proto.DataDTO;
//import com.hugoserve.demo.proto.HistoricPriceDTO;
//import com.hugoserve.demo.proto.entity.HistoricPriceEntity;
//import com.hugoserve.demo.service.Helper.DTOtoEntity;
//import com.hugoserve.demo.utils.DateTimeUtils;
//import com.hugoserve.demo.proto.MetalPriceDTO;
//import com.hugoserve.demo.proto.PriceDTO;
//import com.hugoserve.demo.proto.entity.MarketDataEntity;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class HistoricPriceUpdater {
//
//    private SpotPriceDAO spotPriceDAO;
//    private RestTemplate restTemplate;
//    private final Logger logger= LoggerFactory.getLogger(HistoricPriceUpdater.class);
//    public HistoricPriceUpdater(RestTemplate restTemplate, SpotPriceDAO spotPriceDAO) {
//        this.restTemplate = restTemplate;
//        this.spotPriceDAO = spotPriceDAO;
//    }
//
//    @Scheduled(fixedRate = 4000*60*60)
//    public  void HisoricDataUpdate() throws SQLException,NullPointerException{
//        HistoricPriceDTO historicPriceDTO=
//                restTemplate.getForObject("https://goldbroker.com/api/historical-spot-prices?metal=XAG&currency=PKR&weight_unit=g",
//                        HistoricPriceDTO.class);
//        HistoricPriceEntity historicPriceEntity = spotPriceDAO.historicPriceCurrent(historicPriceDTO.getMetal());
//
//        if (historicPriceEntity.getMetal().isEmpty()) {
//            for (DataDTO dataDTO : historicPriceDTO.getEmbedded().getItemsList() ){
//                HistoricPriceEntity historicPriceEntity1= DTOtoEntity.historicPriceEntity(historicPriceDTO.getMetal(),dataDTO);
//                spotPriceDAO.historicPriceUpdate(historicPriceEntity1);
//                logger.info("All the Data got Addded");
//            }
//        }
//        else {
//            List<DataDTO> historicList = new ArrayList<>(historicPriceDTO.getEmbedded().getItemsList());
//            Collections.reverse(historicList);
//            for(DataDTO dataDTO: historicList){
//                Timestamp dateTimeStr1 = historicPriceEntity.getDate();
//                Timestamp dateTimeStr2 = DateTimeUtils.convertToTimestamp(dataDTO.getDate());
//                int isAfter= DateTimeUtils.compareTimestamps(dateTimeStr1,dateTimeStr2);
//                if (isAfter<0) {
//                    HistoricPriceEntity historicPriceEntity1=DTOtoEntity.historicPriceEntity(historicPriceDTO.getMetal(),dataDTO);
//                    spotPriceDAO.historicPriceUpdate(historicPriceEntity1);
//                    logger.info("A new data got added");
//                }
//                else{
//                    break;
//                }
//            }
//        }
//    }
//
//
//}
//
