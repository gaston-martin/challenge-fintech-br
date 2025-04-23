package com.fintech.challenge.model;

import com.fintech.challenge.util.MovementType;

import java.time.LocalDateTime;

public record Movement(Long id, MovementType type, Long walletId, Double amount, LocalDateTime createdAt, String reference, Long relatedMovementId) {}
