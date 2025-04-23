package com.fintech.challenge.services;

import com.fintech.challenge.entities.BalanceEntity;
import com.fintech.challenge.entities.WalletEntity;
import com.fintech.challenge.exceptions.client.ConcurrentModificationException;
import com.fintech.challenge.exceptions.client.InsufficientSavingsException;
import com.fintech.challenge.exceptions.client.WalletNotFoundException;
import com.fintech.challenge.mappers.BalanceMapper;
import com.fintech.challenge.model.Balance;
import com.fintech.challenge.repositories.BalanceRepository;
import com.fintech.challenge.repositories.WalletRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final WalletRepository walletRepository;
    private final BalanceMapper balanceMapper;

    @Autowired
    public BalanceService(BalanceRepository balanceRepository, WalletRepository walletRepository, BalanceMapper balanceMapper) {
        this.balanceRepository = balanceRepository;
        this.walletRepository = walletRepository;
        this.balanceMapper = balanceMapper;
    }

    public void createBalance(Long id, Double initialBalance){

        Optional<WalletEntity> wallet = walletRepository.findById(id);
        if(wallet.isEmpty()){
            // This can't happen due to the FK in the table.
            throw new WalletNotFoundException("Wallet with id " + id + " not found");
        }

        BalanceEntity balanceEntity = new BalanceEntity();

        balanceEntity.setWallet(wallet.get());
        balanceEntity.setBalance(initialBalance);
        balanceEntity.setLastMovementId(0L);
        balanceEntity.setUpdatedAt(LocalDateTime.now());

        balanceRepository.save(balanceEntity);
    }

    public Optional<Balance> getCurrentBalance(Long id) {
        return balanceRepository
                .findById(id)
                .map(balanceMapper::entityToModel);
    }

    public Balance updateBalance(Long id, Double amountToAdd){
        Optional<BalanceEntity> maybeBalance = balanceRepository.findById(id);
        if(maybeBalance.isEmpty()){
            throw new WalletNotFoundException("Wallet with id " + id + " not found");
        }
        BalanceEntity balanceEntity = maybeBalance.get();
        // Here we hold the current balance and trust the optimistic locking mechanism (which uses version column)
        // to avoid double spend or getting a negative balance because of undetected concurrent withdrawals.
        Double currentAmount = balanceEntity.getBalance();
        Double updatedAmount = currentAmount + amountToAdd;
        // Only perform this check when subtracting money
        if (amountToAdd < 0 && updatedAmount < 0 ) {
            throw new InsufficientSavingsException(
                    "Insufficient savings (" + currentAmount + ") for wallet with id " + id + " and amount " + amountToAdd);
        }
        balanceEntity.setBalance(updatedAmount);
        balanceEntity.setUpdatedAt(LocalDateTime.now());
        BalanceEntity updatedBalance;
        try {
            updatedBalance = balanceRepository.save(balanceEntity);
        } catch (OptimisticLockException e) {
            throw new ConcurrentModificationException(
                    "Concurrent modification detected for wallet with id " + id);
        }
        return balanceMapper.entityToModel(updatedBalance);
    }
}
