package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class CurrencyMismatchException extends ClientException {
    public CurrencyMismatchException(String message) {
        super(message);
    }
}
