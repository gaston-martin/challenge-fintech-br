package com.fintech.challenge.mappers;

import com.fintech.challenge.api.ApiBalance;
import com.fintech.challenge.entities.BalanceEntity;
import com.fintech.challenge.model.Balance;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class BalanceMapper {

    public ApiBalance modelToApi(@NonNull Balance balance){
        return new ApiBalance(
                balance.id(),
                balance.amount(), balance.currency()
        );
    }

    public Balance entityToModel(@NonNull BalanceEntity balanceEntity){
        return new Balance(
                balanceEntity.getId(),
                balanceEntity.getBalance(),
                balanceEntity.getWallet().getCurrency()
        );
    }
}
