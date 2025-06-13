package com.hugoserve.demo.service.impl;

import java.util.UUID;

import com.google.protobuf.Any;
import com.hugoserve.demo.Exception.BadException;
import com.hugoserve.demo.Exception.InternalServerError;
import com.hugoserve.demo.Exception.WalletException;
import com.hugoserve.demo.constants.AppConstants;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.dao.SpotPriceDAO;
import com.hugoserve.demo.dao.TransactionDao;
import com.hugoserve.demo.dao.WalletDao;
import com.hugoserve.demo.facade.SpotPricefacade;
import com.hugoserve.demo.facade.TransactionFacade;
import com.hugoserve.demo.facade.WalletFacade;
import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.AssetTrasactionRequestDTO;
import com.hugoserve.demo.proto.BuyRequestDTO;
import com.hugoserve.demo.proto.SellRequestDTO;
import com.hugoserve.demo.proto.TransactionRequest;
import com.hugoserve.demo.proto.entity.AssetEntity;
import com.hugoserve.demo.proto.entity.AssetTransactionEntity;
import com.hugoserve.demo.proto.entity.MarketDataEntity;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.proto.entity.WalletEntity;
import com.hugoserve.demo.proto.entity.WalletTransactionEntity;
import com.hugoserve.demo.proto.response.AssetTrasactionResponseDTO;
import com.hugoserve.demo.provider.QueueProvider;
import com.hugoserve.demo.provider.QueueService;
import com.hugoserve.demo.service.Helper.DTOtoEntity;
import com.hugoserve.demo.service.Helper.EntityToDTO;
import com.hugoserve.demo.service.TransactionService;
import com.hugoserve.demo.service.WalletService;
import com.hugoserve.demo.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionFacade transactionFacade;
    private final WalletFacade walletFacade;
    private final QueueProvider queueProvider;
    private final SpotPricefacade spotPricefacade;
    private final WalletService walletService;
    private Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    TransactionServiceImpl(TransactionFacade transactionFacade, WalletFacade walletFacade, QueueProvider queueProvider,
                           SpotPricefacade spotPricefacade, WalletService walletService) {
        this.transactionFacade = transactionFacade;
        this.walletFacade = walletFacade;
        this.queueProvider = queueProvider;
        this.spotPricefacade = spotPricefacade;
        this.walletService = walletService;
    }

    @Override
    public ResponseEntity<ApiResponse> buyTransacionService(UserEntity userEntity, BuyRequestDTO buyRequestDTO) {

        WalletEntity walletEntity =
                walletFacade.getWallet(userEntity.getUsername()).orElseThrow(() -> new WalletException(StatusCodes.ERROR_WALLET_NOT_FOUND));

        String metal = buyRequestDTO.getMetal();
        double amount = buyRequestDTO.getAmount();
        MarketDataEntity marketDataEntity = spotPricefacade.spotPriceCurrent(metal);

        if (amount > walletEntity.getBalance()) {
            throw new BadException(StatusCodes.ERROR_INSUFFICIENT_FUNDS);
        }

        try {
            String transaction_id = buyTransaction(userEntity, marketDataEntity, buyRequestDTO, walletEntity, amount);
            AssetTransactionEntity assetTransactionEntity = transactionFacade.getTransactionByID(transaction_id);
            AssetTrasactionResponseDTO assetTrasactionResponseDTO = EntityToDTO.serializetoAssetTransactionResponseDTO(assetTransactionEntity);
            return ResponseUtils.buildResponse(HttpStatus.OK, true, StatusCodes.ORDER_SUCESSFULL, Any.pack(assetTrasactionResponseDTO));
        } catch (Exception e) {
            throw new InternalServerError(StatusCodes.ERROR_INTERNAL_SERVER);
        }
    }

    @Override
    public ResponseEntity<ApiResponse> sellTransacionService(UserEntity userEntity, SellRequestDTO sellRequestDTO) {
        AssetEntity assetEntity = transactionFacade.assetBalance(userEntity, sellRequestDTO.getMetal())
                .orElseThrow(() -> new BadException(StatusCodes.ERROR_METAL_NOT_PRESENT_ASSEST));

        String metal = sellRequestDTO.getMetal();
        double amount = sellRequestDTO.getAmount();
        MarketDataEntity marketDataEntity = spotPricefacade.spotPriceCurrent(metal);

        if (amount / marketDataEntity.getSellPrice() > assetEntity.getBalanceG()) {
            throw new BadException(StatusCodes.ERROR_INSUFFICIENT_METAL);
        }

        try {
            String transaction_id = sellTransaction(userEntity, marketDataEntity, sellRequestDTO, amount);
            AssetTransactionEntity assetTransactionEntity = transactionFacade.getTransactionByID(transaction_id);
            AssetTrasactionResponseDTO assetTrasactionResponseDTO = EntityToDTO.serializetoAssetTransactionResponseDTO(assetTransactionEntity);
            return ResponseUtils.buildResponse(HttpStatus.OK, true, StatusCodes.ORDER_SUCESSFULL, Any.pack(assetTrasactionResponseDTO));
        } catch (Exception e) {
            throw new InternalServerError(StatusCodes.ERROR_INTERNAL_SERVER);
        }
    }

    @Override
    public boolean doTransaction(TransactionRequest transactionRequest) {
        String transactionId = transactionRequest.getTransactionId();
        AssetTransactionEntity assetTransactionEntity = transactionFacade.getTransactionByID(transactionId);
        if (assetTransactionEntity.getTransactionType().equals("BUY")) {
            return buyTransaction(assetTransactionEntity);
        }
        if (assetTransactionEntity.getTransactionType().equals("SELL")) {
            return sellTransaction(assetTransactionEntity);
        }
        return false;
    }

    @Transactional
    public boolean buyTransaction(AssetTransactionEntity assetTransactionEntity) {
        MarketDataEntity marketDataEntity = spotPricefacade.spotPriceCurrent(assetTransactionEntity.getMetal());
        UserEntity userEntity = UserEntity.newBuilder().setUsername(assetTransactionEntity.getUsername()).build();
        if (marketDataEntity.getBuyPrice() != assetTransactionEntity.getPrice()) {
            return false;
        }
        transactionFacade.creditAsset(assetTransactionEntity);
        transactionFacade.updateTransactionStatus(assetTransactionEntity, AppConstants.SUCCESS);
        return true;
    }

    @Transactional
    public boolean sellTransaction(AssetTransactionEntity assetTransactionEntity) {
        MarketDataEntity marketDataEntity = spotPricefacade.spotPriceCurrent(assetTransactionEntity.getMetal());
        UserEntity userEntity = UserEntity.newBuilder().setUsername(assetTransactionEntity.getUsername()).build();
        if (marketDataEntity.getSellPrice() != assetTransactionEntity.getPrice()) {
            return false;
        }
        transactionFacade.debitAssetOnHold(assetTransactionEntity);
        double amount = assetTransactionEntity.getPrice() * assetTransactionEntity.getQuantity();
        String walletId = walletFacade.getWalletId(userEntity.getUsername());
        walletFacade.creditAmount(walletId, amount);
        WalletTransactionEntity walletTransactionEntity = WalletTransactionEntity.newBuilder()
                .setTransactionId(UUID.randomUUID().toString())
                .setAmount(amount)
                .setTransactionType(AppConstants.CREDIT)
                .setStatus(AppConstants.SUCCESS)
                .build();
        walletFacade.createTransaction(walletTransactionEntity);
        transactionFacade.updateTransaction(assetTransactionEntity, marketDataEntity.getSellPrice(), walletTransactionEntity, AppConstants.SUCCESS);
        return true;

    }


    @Transactional
    public String buyTransaction(UserEntity userEntity, MarketDataEntity marketDataEntity, BuyRequestDTO buyRequestDTO, WalletEntity walletEntity,
                                 Double amount) {

        walletFacade.debitAmount(walletEntity.getWalletId(), amount);
        String walletTransactionID = UUID.randomUUID().toString();
        WalletTransactionEntity walletTransactionEntity = WalletTransactionEntity.newBuilder()
                .setTransactionId(walletTransactionID)
                .setWalletId(walletEntity.getWalletId())
                .setAmount(amount)
                .setTransactionType(AppConstants.DEBIT)
                .setStatus(AppConstants.SUCCESS)
                .build();
        walletFacade.createTransaction(walletTransactionEntity);

        AssetTrasactionRequestDTO assetTrasactionRequestDTO = AssetTrasactionRequestDTO.newBuilder()
                .setUsername(userEntity.getUsername())
                .setMetal(buyRequestDTO.getMetal())
                .setQuantity(amount / marketDataEntity.getBuyPrice())
                .setPrice(marketDataEntity.getBuyPrice())
                .setType(AppConstants.BUY)
                .setWalletTransactionId(walletTransactionEntity.getTransactionId())
                .build();
        AssetTransactionEntity assetTransactionEntity = DTOtoEntity.assetTransactionEntityCreate(assetTrasactionRequestDTO);
        String transactionId = transactionFacade.createTransaction(userEntity, assetTransactionEntity);
        queueProvider.pushToFifo(TransactionRequest.newBuilder().setTransactionId(transactionId).build(), userEntity.getUsername(), 0);
        return transactionId;
    }

    @Transactional
    public String sellTransaction(UserEntity userEntity, MarketDataEntity marketDataEntity, SellRequestDTO sellRequestDTO, Double amount) {
        AssetTrasactionRequestDTO assetTrasactionRequestDTO = AssetTrasactionRequestDTO.newBuilder()
                .setUsername(userEntity.getUsername())
                .setMetal(sellRequestDTO.getMetal())
                .setQuantity(amount / marketDataEntity.getSellPrice())
                .setPrice(marketDataEntity.getSellPrice())
                .setType(AppConstants.SELL)
                .build();

        AssetTransactionEntity assetTransactionEntity = DTOtoEntity.assetTransactionEntityCreate(assetTrasactionRequestDTO);
        transactionFacade.debitAsset(assetTransactionEntity);
        String transactionId = transactionFacade.createTransaction(userEntity, assetTransactionEntity);
        queueProvider.pushToFifo(TransactionRequest.newBuilder().setTransactionId(transactionId).build(), userEntity.getUsername(), 0);
        return transactionId;
    }

}
