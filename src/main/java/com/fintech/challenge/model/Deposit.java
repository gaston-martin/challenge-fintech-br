package com.fintech.challenge.model;

import java.util.Optional;

public record Deposit(Long walletId, Double amount, Optional<String> reference) {}
