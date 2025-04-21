package com.fintech.challenge.exceptions;

public abstract class ClientException extends RuntimeException {
    public ClientException(String message) {
        super(message);
    }
}
