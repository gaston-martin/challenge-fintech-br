package com.fintech.challenge.exceptions.client;

import com.fintech.challenge.exceptions.ClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class InvalidUserIdException extends ClientException {
    public InvalidUserIdException(String message) {
        super(message);
    }
}
