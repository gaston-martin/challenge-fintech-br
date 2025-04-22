package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class InvalidCurrencyException extends ClientException {
    public InvalidCurrencyException(String message) {
        super(message);
    }
}
