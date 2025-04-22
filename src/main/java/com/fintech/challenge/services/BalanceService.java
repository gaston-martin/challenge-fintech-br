package com.fintech.challenge.services;

import com.fintech.challenge.entities.BalanceEntity;
import com.fintech.challenge.entities.WalletEntity;
import com.fintech.challenge.exceptions.client.WalletNotFoundException;
import com.fintech.challenge.mappers.BalanceMapper;
import com.fintech.challenge.model.Balance;
import com.fintech.challenge.repositories.BalanceRepository;
import com.fintech.challenge.repositories.WalletRepository;
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
        balanceEntity.setVersion(1L);
        balanceEntity.setUpdatedAt(LocalDateTime.now());

        balanceRepository.save(balanceEntity);
    }

    public Optional<Balance> getCurrentBalance(Long id) {
        return balanceRepository
                .findById(id)
                .map(balanceMapper::entityToModel);
    }
}
