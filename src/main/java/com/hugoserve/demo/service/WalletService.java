package com.hugoserve.demo.service;

import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.AuthRequestDTO;
import com.hugoserve.demo.proto.UserDetailsDTO;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface WalletService {

    ResponseEntity<ApiResponse> rechargeWallet(UserEntity userEntity, double amount);

    ResponseEntity<ApiResponse> getBalance(UserEntity userEntity);


}
