package com.fintech.challenge.exceptions.server;

import com.fintech.challenge.exceptions.ServerException;

public class TooManyResultsException extends ServerException {
    public TooManyResultsException(String message) {
        super(message);
    }
}
