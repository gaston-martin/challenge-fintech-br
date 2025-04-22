package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class InvalidCountryException extends ClientException {
    public InvalidCountryException(String message) {
        super(message);
    }
}
