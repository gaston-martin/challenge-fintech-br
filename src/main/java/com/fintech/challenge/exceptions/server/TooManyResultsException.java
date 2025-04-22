package com.fintech.challenge.exceptions.server;

import com.fintech.challenge.exceptions.ServerException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR)
public class TooManyResultsException extends ServerException {
    public TooManyResultsException(String message) {
        super(message);
    }
}
