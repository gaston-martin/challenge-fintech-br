package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class InvalidTimeException extends ClientException {
    public InvalidTimeException(String message) {
        super(message);
    }
}
