package com.fintech.challenge.controllers;
import com.fintech.challenge.api.CreatedWallet;
import com.fintech.challenge.api.ApiWallet;
import com.fintech.challenge.exceptions.client.InvalidUserIdException;
import com.fintech.challenge.mappers.WalletMapper;
import com.fintech.challenge.model.Wallet;
import com.fintech.challenge.services.BalanceService;
import com.fintech.challenge.services.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {


    private final WalletService walletService;
    private final BalanceService balanceService;

    private final WalletMapper mapper;

    @Autowired
    public WalletController(WalletService walletService, BalanceService balanceService, WalletMapper mapper) {
        this.walletService = walletService;
        this.balanceService = balanceService;
        this.mapper = mapper;
    }


    @PostMapping
    @ResponseBody
    @Transactional(rollbackFor = Throwable.class)
    public  CreatedWallet createWallet(@RequestBody ApiWallet wallet) {

        // Validate given fields
        validateWalletData(wallet);

        // For idempotency control, figure out if there is already an existent wallet or create a new one
        var walletOptional = walletService.findWalletByUserIdCountryAndCurrency(wallet.userId(), wallet.country(), wallet.currency());
        if (walletOptional.isPresent()) {
            // There is an existent wallet for the given parameters.
            // Behave idempotent
            return mapper.modelToApi(walletOptional.get());
        } else {
            // Create a new Wallet along with its balance
            Wallet walletToCreate = mapper.apiToModel(wallet);
            Wallet walletInsertedAsModel = walletService.insertNewWallet(walletToCreate);
            balanceService.createBalance(walletInsertedAsModel.id(), 0.0);
            return mapper.modelToApi(walletInsertedAsModel);
        }
    }

    @GetMapping
    @ResponseBody
    public List<CreatedWallet> getAllWallets(@RequestParam(required = false) String userId){
        validateUserId(userId);
        return walletService
                .findAllWalletsByUserId(userId)
                .stream()
                .map(mapper::modelToApi)
                .collect(Collectors.toList());
    }

    private void validateWalletData(ApiWallet apiWallet) {
        // This will throw an exception if any field is invalid
        mapper.apiToModel(apiWallet);
    }

    private void validateUserId(String userId) {
        if (userId == null) {
            throw new InvalidUserIdException("Missing userId");
        }
    }
}
