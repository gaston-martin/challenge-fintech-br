package com.fintech.challenge.services;

import com.fintech.challenge.entities.MovementEntity;
import com.fintech.challenge.entities.WalletEntity;
import com.fintech.challenge.exceptions.client.WalletNotFoundException;
import com.fintech.challenge.mappers.MovementMapper;
import com.fintech.challenge.model.Movement;
import com.fintech.challenge.repositories.MovementRepository;
import com.fintech.challenge.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MovementService {

    private final MovementRepository movementRepository;
    private final WalletRepository walletRepository;
    private final MovementMapper movementMapper;
//    private final BalanceService balanceService;


    @Autowired
    public MovementService(MovementRepository movementRepository, WalletRepository walletRepository, MovementMapper movementMapper) {
        this.movementRepository = movementRepository;
        this.walletRepository = walletRepository;
        this.movementMapper = movementMapper;
    }

    public Movement insertMovement(Movement movement){
        WalletEntity wallet = getWallet(movement.walletId());

        MovementEntity movementEntity = movementMapper.modelToEntity(movement);
        movementEntity.setWallet(wallet); // It's important because it's a table join
        // Insert movement in database
        MovementEntity savedEntity = movementRepository.save(movementEntity);

        return movementMapper.entityToModel(savedEntity);
    }

    public Double getBalanceAtTime(Long walletId, LocalDateTime time){
        return movementRepository.findSumByWalletIdAndPointInTime(walletId, time);
    }



    private WalletEntity getWallet(Long walletId) {
        Optional<WalletEntity> wallet = walletRepository.findById(walletId);
        if(wallet.isEmpty()){
            // This can't happen due to the FK in the table.
            throw new WalletNotFoundException("Wallet with id " + walletId + " not found");
        }
        return wallet.get();
    }
}
