package com.fintech.challenge.model;

import com.fintech.challenge.util.Country;
import com.fintech.challenge.util.Currency;

public record Wallet(Long id, String userId, Currency currency, Country country) {}
