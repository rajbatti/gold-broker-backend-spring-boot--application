package com.hugoserve.demo.controller;

import com.hugoserve.demo.model.UserData;
import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.proto.BuyRequestDTO;
import com.hugoserve.demo.proto.SellRequestDTO;
import com.hugoserve.demo.proto.entity.UserEntity;
import com.hugoserve.demo.service.Helper.DTOtoEntity;
import com.hugoserve.demo.service.PriceService;
import com.hugoserve.demo.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
    private final PriceService priceService;
    private final TransactionService transactionService;

    TransactionController(PriceService priceService, TransactionService transactionService) {
        this.priceService = priceService;
        this.transactionService = transactionService;
    }

    @GetMapping(value = "/price", params = "type=regular")
    public ResponseEntity<ApiResponse> getLatestPriceOfMetal(@RequestParam String metal) {
        return priceService.getLatestPriceByMetal(metal);
    }

    @GetMapping(value = "/price", params = "type=paginated")
    public ResponseEntity<ApiResponse> getLatestPriceOfMetalPaginated(@RequestParam String metal, @RequestParam int page_number,
                                                                      @RequestParam int records) {
        return priceService.getPriceByMetalAndPaginated(metal, page_number, records);
    }

    @PostMapping("/buy")
    public ResponseEntity<ApiResponse> buyAsset(@RequestBody BuyRequestDTO buyRequestDTO) {
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = DTOtoEntity.userEntityCreate(userData);
        return transactionService.buyTransacionService(userEntity, buyRequestDTO);
    }

    @PostMapping("/sell")
    public ResponseEntity<ApiResponse> sellAsset(@RequestBody SellRequestDTO sellRequestDTO) {
        UserData userData = (UserData) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity userEntity = DTOtoEntity.userEntityCreate(userData);
        return transactionService.sellTransacionService(userEntity, sellRequestDTO);
    }
}
