package com.fintech.challenge.controllers;

import com.fintech.challenge.api.ApiBalance;
import com.fintech.challenge.exceptions.client.WalletNotFoundException;
import com.fintech.challenge.mappers.BalanceMapper;
import com.fintech.challenge.model.Balance;
import com.fintech.challenge.services.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/")
public class BalanceController {

    private final BalanceService balanceService;
    private final BalanceMapper mapper;

    @Autowired
    public BalanceController(BalanceService balanceService, BalanceMapper mapper) {
        this.balanceService = balanceService;
        this.mapper = mapper;
    }

    @GetMapping(path="/wallets/{id}/balance")
    public ApiBalance getBalance(@PathVariable("id") Long walletId ){
        Optional<Balance> maybeBalance = balanceService.getCurrentBalance(walletId);
        if(maybeBalance.isPresent()){
            return mapper.modelToApi(maybeBalance.get());
        }
        throw new WalletNotFoundException("Wallet with id " + walletId + " not found");
    }

    /*     public List<CreatedWallet> getAllWallets(@RequestParam(required = false) String userId){
        validateUserId(userId);
        return walletService
                .findAllWalletsByUserId(userId)
                .stream()
                .map(mapper::modelToApi)
                .collect(Collectors.toList());
    }
*/


}
