package com.fintech.challenge.api;

import java.util.Optional;

public record ApiWithdrawal(Double amount, String currency, Optional<String> reference) {}
