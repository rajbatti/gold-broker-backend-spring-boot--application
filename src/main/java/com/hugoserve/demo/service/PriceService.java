package com.hugoserve.demo.service;

import com.hugoserve.demo.proto.ApiResponse;
import org.springframework.http.ResponseEntity;

public interface PriceService {
    ResponseEntity<ApiResponse> getLatestPriceByMetal(String metal);

    ResponseEntity<ApiResponse> getPriceByMetalAndPaginated(String metal, int pageNumber, int record);
}
