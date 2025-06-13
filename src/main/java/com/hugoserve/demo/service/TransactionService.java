package com.hugoserve.demo.service;

import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.AssetTrasactionRequestDTO;
import com.hugoserve.demo.proto.BuyRequestDTO;
import com.hugoserve.demo.proto.SellRequestDTO;
import com.hugoserve.demo.proto.TransactionRequest;
import com.hugoserve.demo.proto.entity.UserEntity;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<ApiResponse> buyTransacionService(UserEntity userEntity, BuyRequestDTO buyRequestDTO);

    ResponseEntity<ApiResponse> sellTransacionService(UserEntity userEntity, SellRequestDTO sellRequestDTO);

    boolean doTransaction(TransactionRequest transactionRequest);
}
