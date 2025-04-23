package com.fintech.challenge.api;

import java.util.Optional;

public record ApiMoneyTransfer(Long payerWalletId, Long collectorWalletId, Double amount, String currency, Optional<String> reference) {}
