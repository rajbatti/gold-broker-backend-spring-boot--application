package com.hugoserve.demo.Exception;

import com.hugoserve.demo.constants.StatusCodes;

public class WalletException extends RuntimeException {
    private final StatusCodes statusCodes;

    public WalletException(StatusCodes statusCodes) {
        super(statusCodes.getErrorMessage());
        this.statusCodes = statusCodes;
    }

    public StatusCodes getStatusCode() {
        return statusCodes;
    }
}
