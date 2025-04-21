package com.fintech.challenge.util;

import com.fintech.challenge.exceptions.client.InvalidCurrencyException;

public enum Currency {
    ARS,
    BOB,
    BRL,
    CLP,
    COP,
    MXN,
    PEN,
    PYG,
    UYU,
    VEN,
    USD;

    public static Currency fromString(String currencyCode) {
        Currency currency;
        try {
            currency = Currency.valueOf(currencyCode);
        } catch (IllegalArgumentException e) {
            throw new InvalidCurrencyException(
                    String.format("Invalid currency: %s", currencyCode));
        }
        return currency;
    }
}
