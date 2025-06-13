package com.hugoserve.demo.Exception;

import com.hugoserve.demo.constants.StatusCodes;

public class UserException extends RuntimeException {

    private final StatusCodes statusCodes;

    public UserException(StatusCodes statusCodes) {
        super(statusCodes.getErrorMessage());
        this.statusCodes = statusCodes;
    }

    public StatusCodes getStatusCode() {
        return statusCodes;
    }


}