package com.hugoserve.demo.Exception.handler;


import com.hugoserve.demo.Exception.BadException;
import com.hugoserve.demo.Exception.DatabaseException;
import com.hugoserve.demo.Exception.InternalServerError;
import com.hugoserve.demo.Exception.UserException;
import com.hugoserve.demo.Exception.WalletException;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.proto.ApiResponse;
import com.hugoserve.demo.utils.ResponseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(value = {UserException.class})
    public ResponseEntity<ApiResponse> handleException(UserException e) {
        return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, false, e.getStatusCode());
    }

    @ExceptionHandler(value = {InternalServerError.class})
    public ResponseEntity<ApiResponse> handleException(InternalServerError e) {
        return ResponseUtils.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, e.getStatusCode());
    }

    @ExceptionHandler(value = {WalletException.class})
    public ResponseEntity<ApiResponse> handleException(WalletException e) {
        return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, false, e.getStatusCode());
    }

    @ExceptionHandler(value = {DatabaseException.class})
    public ResponseEntity<ApiResponse> handleException() {
        return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, false, StatusCodes.ERROR_INTERNAL_SERVER);
    }

    @ExceptionHandler(value = {BadException.class})
    public ResponseEntity<ApiResponse> handleException(BadException e) {
        return ResponseUtils.buildResponse(HttpStatus.BAD_REQUEST, false, e.getStatusCode());
    }


}
