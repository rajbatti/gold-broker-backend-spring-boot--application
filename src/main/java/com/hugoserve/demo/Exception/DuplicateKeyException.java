package com.hugoserve.demo.Exception;

public class DuplicateKeyException extends Exception {

    public DuplicateKeyException(Exception e) {
        super(e);
    }

    public DuplicateKeyException(String message, Exception e) {
        super(message, e);
    }

    public DuplicateKeyException(String message) {
        super(message);
    }
}
