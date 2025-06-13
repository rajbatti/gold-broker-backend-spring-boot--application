package com.hugoserve.demo.service.impl;

import java.util.UUID;

import com.google.protobuf.Any;
import com.hugoserve.demo.Exception.WalletException;
import com.hugoserve.demo.constants.AppConstants;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.facade.WalletFacade;
import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import com.hugoserve.demo.proto.response.WalletResponseDTO;
import com.hugoserve.demo.proto.response.WalletTrasactionResponseDTO;
import com.hugoserve.demo.service.Helper.EntityToDTO;
import com.hugoserve.demo.service.WalletService;
import com.hugoserve.demo.utils.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletFacade walletFacade;

    WalletServiceImpl(WalletFacade walletFacade) {
        this.walletFacade = walletFacade;
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> rechargeWallet(UserEntity userEntity, double amount) {
        WalletEntity walletEntity =
                walletFacade.getWallet(userEntity.getUsername()).orElseThrow(() -> new WalletException(StatusCodes.ERROR_WALLET_NOT_FOUND));

        String walletTransactionId = UUID.randomUUID().toString();
        WalletTransactionEntity walletTransactionEntity = WalletTransactionEntity.newBuilder().setTransactionId(walletTransactionId)
                .setWalletId(walletEntity.getWalletId())
                .setAmount(amount)
                .setStatus(AppConstants.PENDING)
                .setTransactionType(AppConstants.CREDIT)
                .build();

        walletFacade.createTransaction(walletTransactionEntity);

        try {
            walletFacade.creditAmount(walletTransactionEntity.getWalletId(), walletTransactionEntity.getAmount());
            walletFacade.updateWalletTransaction(walletTransactionEntity.getTransactionId(), AppConstants.SUCCESS);
        } catch (Exception e) {
            walletFacade.updateWalletTransaction(walletTransactionEntity.getTransactionId(), AppConstants.FAILED);
        }

        walletTransactionEntity = walletFacade.getWalletTransactionById(walletTransactionEntity.getTransactionId());
        walletEntity = walletFacade.balance(walletTransactionEntity.getWalletId());
        WalletTrasactionResponseDTO walletTrasactionResponseDTO = EntityToDTO.serializetoWalletResponseDTO(walletTransactionEntity, walletEntity);
        return ResponseUtils.buildResponse(HttpStatus.OK, true, StatusCodes.SUCCESS_BALANCE_FETCHED, Any.pack(walletTrasactionResponseDTO));

    }

    @Override
    public ResponseEntity<ApiResponse> getBalance(UserEntity userEntity) {
        String walletId = walletFacade.getWalletId(userEntity.getUsername());
        WalletEntity walletEntity = walletFacade.balance(walletId);
        WalletResponseDTO walletResponseDTO = EntityToDTO.serializetoWalletResponseDTO(walletEntity);
        return ResponseUtils.buildResponse(HttpStatus.OK, true, StatusCodes.SUCCESS_BALANCE_FETCHED, Any.pack(walletResponseDTO));
    }


}
