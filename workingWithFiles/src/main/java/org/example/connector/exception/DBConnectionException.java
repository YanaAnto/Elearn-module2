package org.example.connector.exception;

public class DBConnectionException extends RuntimeException {

    public DBConnectionException(String message) {
        super(message);
    }
}
