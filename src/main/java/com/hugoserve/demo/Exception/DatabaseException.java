package com.hugoserve.demo.Exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(Exception e) {
        super(e);
    }

    public DatabaseException(String message, Exception e) {
        super(e);
    }

    public DatabaseException(String message) {
        super(message);
    }
}
