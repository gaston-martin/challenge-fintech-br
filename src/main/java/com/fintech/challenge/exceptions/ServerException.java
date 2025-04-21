package com.fintech.challenge.exceptions;

public abstract class ServerException extends RuntimeException {
    public ServerException(String message) {
        super(message);
    }
}
