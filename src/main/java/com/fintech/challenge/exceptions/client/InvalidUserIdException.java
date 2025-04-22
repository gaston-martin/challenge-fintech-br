package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class InvalidUserIdException extends ClientException {
    public InvalidUserIdException(String message) {
        super(message);
    }
}
