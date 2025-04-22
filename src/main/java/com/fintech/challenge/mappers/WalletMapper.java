package com.fintech.challenge.mappers;

import com.fintech.challenge.api.ApiWallet;
import com.fintech.challenge.api.CreatedWallet;
import com.fintech.challenge.entities.WalletEntity;
import com.fintech.challenge.exceptions.client.InvalidUserIdException;
import com.fintech.challenge.model.Wallet;
import com.fintech.challenge.util.Country;
import com.fintech.challenge.util.Currency;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper {

    public Wallet apiToModel(@NonNull ApiWallet apiWallet ){

        String userId = apiWallet.userId();
        if (userId == null || userId.isBlank()) {
            throw new InvalidUserIdException(
                String.format("Invalid userId: %s", apiWallet.userId()));
        }

        return new com.fintech.challenge.model.Wallet(0L, userId, Currency.fromString(apiWallet.currency()), Country.fromString(apiWallet.country()));
    }

    public CreatedWallet modelToApi(@NonNull com.fintech.challenge.model.Wallet wallet ){
        return new CreatedWallet(
                wallet.id(),
                wallet.userId(),
                wallet.currency().name(),
                wallet.country().name()
        );
    }

    public Wallet entityToModel(@NonNull WalletEntity walletEntity){
        return new Wallet(
                walletEntity.getId(),
                walletEntity.getUserId(),
                Currency.fromString(walletEntity.getCurrency()),
                Country.fromString(walletEntity.getCountry())
        );
    }
    
    public WalletEntity modelToEntity(@NonNull Wallet wallet ){
        // Entity has only a no-args constructor
        WalletEntity walletEntity = new WalletEntity();

        walletEntity.setUserId(wallet.userId());
        walletEntity.setCurrency(wallet.currency().name());
        walletEntity.setCountry(wallet.country().name());

        return walletEntity;
    }


}
