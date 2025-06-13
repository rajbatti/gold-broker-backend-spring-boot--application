package com.hugoserve.demo.constants;

public enum StatusCodes {

    ERROR_INTERNAL_SERVER("E_100", "INTERNAL SERVER ERROR PLEASE TRY AGAIN AFTER SOMETIME"),
    ERROR_USER_ALREADY_EXISTS("E_002", "Username already exists"),
    SUCCESS_LOGIN("S_003", "Login successful"),
    ERROR_WALLET_NOT_FOUND("E_012", "Wallet not found"),
    ERROR_METAL_NOT_PRESENT_ASSEST("E_013","METAL NOT PRESENT IN YOUR ASSEST"),
    SUCCESS_BALANCE_FETCHED("S_012", "Balance fetched successfully"),
    SUCCESS_FETCH_DATA("S_020", "Market Data fetched successfully"),
    ERROR_INSUFFICIENT_FUNDS("E_019", "Insufficient Funds"),
    ERROR_INSUFFICIENT_METAL("E_201","INSUFFICIENT METAL"),
    ORDER_SUCESSFULL("O__001","ORDER PLACED SUCESSFULLY");

    private final String errorCode;
    private final String errorMessage;
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    StatusCodes(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
