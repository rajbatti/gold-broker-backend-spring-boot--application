package com.hugoserve.demo.service.impl;

import java.util.List;
import java.util.logging.Logger;

import com.google.protobuf.Any;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.dao.SpotPriceDAO;
import com.hugoserve.demo.facade.SpotPricefacade;
import com.hugoserve.demo.proto.MetalDataPaginatedDTO;
import com.hugoserve.demo.service.Helper.EntityToDTO;
import com.hugoserve.demo.service.PriceService;
import com.hugoserve.demo.utils.ResponseUtils;
import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.MetalDataDTO;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PriceServiceImpl implements PriceService {
    private SpotPricefacade spotPricefacade;

    PriceServiceImpl(SpotPricefacade spotPricefacade) {
        this.spotPricefacade = spotPricefacade;
    }

    @Override
    public ResponseEntity<ApiResponse> getLatestPriceByMetal(String metal) {
        MarketDataEntity marketDataEntity = spotPricefacade.spotPriceCurrent(metal);
        MetalDataDTO metalDataDTO = EntityToDTO.serializeToMetalDataDTO(marketDataEntity);
        return ResponseUtils.buildResponse(HttpStatus.OK, true, StatusCodes.SUCCESS_FETCH_DATA, Any.pack(metalDataDTO));
    }

    @Override
    public ResponseEntity<ApiResponse> getPriceByMetalAndPaginated(String metal, int pageNumber, int record) {
        List<MarketDataEntity> spotPriceList = spotPricefacade.getSpotPriceList(metal);
        int startIndex = (pageNumber - 1) * record;
        int lastIndex = startIndex + record;
        List<MetalDataDTO> metalDataDTOList =
                spotPriceList.subList(startIndex, lastIndex).stream().map(spotPrice -> EntityToDTO.serializeToMetalDataDTO(spotPrice)).toList();
        MetalDataPaginatedDTO metalDataPaginatedDTO = MetalDataPaginatedDTO.newBuilder()
                .addAllItems(metalDataDTOList).build();
        return ResponseUtils.buildResponse(HttpStatus.OK, true, StatusCodes.SUCCESS_FETCH_DATA, Any.pack(metalDataPaginatedDTO));
    }
}
