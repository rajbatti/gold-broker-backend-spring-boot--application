package com.hugoserve.demo.controller;

import com.hugoserve.demo.model.UserData;
import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.RechargeDTO;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.service.Helper.DTOtoEntity;
import com.hugoserve.demo.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WalletController {
    private final WalletService walletService;

    WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallet_balance")
    public ResponseEntity<ApiResponse> getBalance() {
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = DTOtoEntity.userEntityCreate(userData);
        return walletService.getBalance(userEntity);
    }

    @PostMapping("/recharge")
    public ResponseEntity<ApiResponse> rechargeWallet(@RequestBody RechargeDTO rechargeDTO) {
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = DTOtoEntity.userEntityCreate(userData);
        return walletService.rechargeWallet(userEntity, rechargeDTO.getAmount());
    }


}
