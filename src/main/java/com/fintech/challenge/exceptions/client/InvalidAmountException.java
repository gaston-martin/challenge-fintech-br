package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class InvalidAmountException extends ClientException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
