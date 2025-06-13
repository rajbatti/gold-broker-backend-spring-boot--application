package com.hugoserve.demo.utils;




import com.google.protobuf.Any;
import com.hugoserve.demo.constants.StatusCodes;
import com.hugoserve.demo.proto.ApiResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    private ResponseUtils() {}

    public static ResponseEntity<ApiResponse> buildResponse(HttpStatus httpStatusCode, boolean isSuccess, StatusCodes status) {
        return ResponseEntity.status(httpStatusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.newBuilder()
                        .setSuccess(isSuccess)
                        .setStatusCode(status.getErrorCode())
                        .setMessage(status.getErrorMessage())
                        .build()
                );
    }

    public static ResponseEntity<ApiResponse> buildResponse(HttpStatus httpStatusCode, boolean isSuccess, StatusCodes status, Any data) {
        return ResponseEntity.status(httpStatusCode)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ApiResponse.newBuilder()
                        .setSuccess(isSuccess)
                        .setStatusCode(status.getErrorCode())
                        .setMessage(status.getErrorMessage())
                        .setData(data)
                        .build()
                );
    }

    public static ResponseEntity<ApiResponse> buildResponse(HttpStatus httpStatusCode, boolean isSuccess, StatusCodes status, Any data, HttpHeaders headers) {
        return ResponseEntity.status(httpStatusCode)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiResponse.newBuilder()
                                .setSuccess(isSuccess)
                                .setStatusCode(status.getErrorCode())
                                .setMessage(status.getErrorMessage())
                                .setData(data)
                                .build()
                );
    }

    public static ResponseEntity<ApiResponse> buildResponse(HttpStatus httpStatusCode, boolean isSuccess, StatusCodes status, HttpHeaders headers) {
        return ResponseEntity.status(httpStatusCode)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        ApiResponse.newBuilder()
                                .setSuccess(isSuccess)
                                .setStatusCode(status.getErrorCode())
                                .setMessage(status.getErrorMessage())
                                .build()
                );
    }

}
