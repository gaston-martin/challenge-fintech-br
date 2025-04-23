package com.fintech.challenge.util;

import com.fintech.challenge.exceptions.server.InvalidMovementTypeException;

public enum MovementType {
    DEPOSIT,
    WITHDRAWAL,
    TRANSFER,
    CASHBACK,
    CHARGEBACK,
    FEE,
    TAX;

    public static MovementType fromString(String stringType) {
        MovementType movementType;
        try {
            movementType = MovementType.valueOf(stringType);
        } catch (IllegalArgumentException e) {
            throw new InvalidMovementTypeException(
                    String.format("Invalid movement type: %s", stringType));
        }
        return movementType;
    }
}




