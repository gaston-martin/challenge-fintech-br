package com.fintech.challenge.repositories;

import com.fintech.challenge.entities.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface WalletRepository extends JpaRepository<WalletEntity, Long> {

    public List<WalletEntity> findByUserIdAndCountryAndCurrency(String userId, String country, String currency);

    public List<WalletEntity> findByUserId(String userId);
}
