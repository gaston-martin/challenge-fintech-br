package com.fintech.challenge.services;

import com.fintech.challenge.entities.WalletEntity;
import com.fintech.challenge.exceptions.server.TooManyResultsException;
import com.fintech.challenge.mappers.WalletMapper;
import com.fintech.challenge.model.Wallet;
import com.fintech.challenge.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService {

    private final WalletRepository repository;
    private final WalletMapper mapper;

    @Autowired
    public WalletService(WalletRepository repository, WalletMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Optional<Wallet> findWalletById(Long id){
        return repository.findById(id)
                .map(mapper::entityToModel);
    }

    public List<Wallet> findAllWalletsByUserId(String userId){
        return repository
                .findByUserId(userId)
                .stream()
                .map(mapper::entityToModel)
                .collect(Collectors.toList());
    }

    private WalletEntity saveWallet(WalletEntity walletEntity){
        return repository.save(walletEntity);
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public Wallet insertNewWallet(Wallet newWallet){
        WalletEntity walletEntity = mapper.modelToEntity(newWallet);
        walletEntity.setCreatedAt(LocalDateTime.now());
        WalletEntity savedWallet = saveWallet(walletEntity);
        return mapper.entityToModel(savedWallet);
    }

    public Optional<Wallet> findWalletByUserIdCountryAndCurrency(String userId, String country, String currency){
        List<WalletEntity> found = repository.findByUserIdAndCountryAndCurrency(userId, country, currency);
        return switch (found.size()) {
            case 0 -> Optional.empty();
            case 1 -> Optional.of(mapper.entityToModel(found.getFirst()));
            default -> throw new TooManyResultsException("Found more than one wallet for the given parameters");
        };
    }
}
