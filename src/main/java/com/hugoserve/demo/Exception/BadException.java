package com.hugoserve.demo.Exception;

import com.hugoserve.demo.constants.StatusCodes;

public class BadException extends RuntimeException {
    private final StatusCodes statusCodes;

    public BadException(StatusCodes statusCodes) {
        super(statusCodes.getErrorMessage());
        this.statusCodes = statusCodes;
    }

    public StatusCodes getStatusCode() {
        return statusCodes;
    }
}
