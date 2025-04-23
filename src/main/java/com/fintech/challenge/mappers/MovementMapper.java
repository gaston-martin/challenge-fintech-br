package com.fintech.challenge.mappers;

import com.fintech.challenge.entities.MovementEntity;
import com.fintech.challenge.model.Movement;
import com.fintech.challenge.util.MovementType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class MovementMapper {


    public Movement entityToModel(@NonNull MovementEntity movementEntity){
        return new Movement(
               movementEntity.getId(),
               MovementType.fromString(movementEntity.getType()),
               movementEntity.getWallet().getId(),
               movementEntity.getAmount(),
               movementEntity.getCreatedAt(),
               movementEntity.getReference(),
               movementEntity.getRelatedMovementId()
        );
    }

    public MovementEntity modelToEntity(@NonNull Movement movement){
        // Entity has only a no-args constructor
        MovementEntity movementEntity = new MovementEntity();
        movementEntity.setType(movement.type().name());
        movementEntity.setAmount(movement.amount());
        movementEntity.setCreatedAt(movement.createdAt());
        movementEntity.setReference(movement.reference());
        movementEntity.setRelatedMovementId(movement.relatedMovementId());

        return movementEntity; // Won't have the WalletEntity set yet.
    }
}
