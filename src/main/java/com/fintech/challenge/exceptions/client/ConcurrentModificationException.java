package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;

public class ConcurrentModificationException extends ClientException {
    public ConcurrentModificationException(String message) {
        super(message);
    }
}
