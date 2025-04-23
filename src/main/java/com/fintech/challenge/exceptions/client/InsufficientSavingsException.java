package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class InsufficientSavingsException extends ClientException {
    public InsufficientSavingsException(String message) {
        super(message);
    }
}
