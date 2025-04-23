package com.fintech.challenge.api;

import java.util.Optional;

public record ApiDeposit(Double amount, String currency, Optional<String> reference) {}
