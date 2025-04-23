package com.fintech.challenge.mappers;

import com.fintech.challenge.api.ApiDeposit;
import com.fintech.challenge.model.Deposit;
import org.springframework.stereotype.Component;

@Component
public class DepositMapper {

    public Deposit apiToModel(ApiDeposit apiDeposit, Long walletId){
        return new Deposit(
                walletId,
                apiDeposit.amount(),
                apiDeposit.reference()
        );
    }
}
