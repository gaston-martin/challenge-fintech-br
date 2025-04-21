package com.fintech.challenge.api;

public record ApiErrorResponse(String timestamp, String error, Integer status, String path ) {}
