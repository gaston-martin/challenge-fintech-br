package com.fintech.challenge.controllers;

import com.fintech.challenge.api.ApiErrorResponse;
import com.fintech.challenge.exceptions.ClientException;
import com.fintech.challenge.exceptions.ServerException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ApiErrorResponse> handleClientException(Exception ex, HttpServletRequest request) {
        String currentTimestamp = LocalDateTime.now().toString();
        String path = request.getRequestURI();
        return ResponseEntity.status(400).body(new ApiErrorResponse(currentTimestamp, ex.getMessage(), 400, path));
    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<ApiErrorResponse> handleServerException(Exception ex, HttpServletRequest request) {
        String currentTimestamp = LocalDateTime.now().toString();
        String path = request.getRequestURI();
        return ResponseEntity.status(500).body(new ApiErrorResponse(currentTimestamp, ex.getMessage(), 500, path));
    }
}
