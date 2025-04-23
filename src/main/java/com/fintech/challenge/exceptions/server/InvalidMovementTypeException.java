package com.fintech.challenge.exceptions.server;

import com.fintech.challenge.exceptions.ServerException;

public class InvalidMovementTypeException extends ServerException {
    public InvalidMovementTypeException(String message) {
        super(message);
    }
}
