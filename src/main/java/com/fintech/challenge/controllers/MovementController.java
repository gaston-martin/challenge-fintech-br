package com.fintech.challenge.controllers;

import com.fintech.challenge.api.ApiBalance;
import com.fintech.challenge.api.ApiDeposit;
import com.fintech.challenge.exceptions.client.CurrencyMismatchException;
import com.fintech.challenge.exceptions.client.InvalidAmountException;
import com.fintech.challenge.exceptions.client.WalletNotFoundException;
import com.fintech.challenge.mappers.BalanceMapper;
import com.fintech.challenge.mappers.DepositMapper;
import com.fintech.challenge.model.Balance;
import com.fintech.challenge.model.Movement;
import com.fintech.challenge.model.Wallet;
import com.fintech.challenge.services.BalanceService;
import com.fintech.challenge.services.MovementService;
import com.fintech.challenge.services.WalletService;
import com.fintech.challenge.util.Currency;
import com.fintech.challenge.util.MovementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
public class MovementController {

    private final MovementService movementService;
    private final WalletService walletService;
    private final BalanceService balanceService;
    private final BalanceController balanceController;
    private final BalanceMapper balanceMapper;
    private final DepositMapper depositMapper;

    @Autowired
    public MovementController(MovementService movementService, WalletService walletService, BalanceService balanceService, BalanceController balanceController, BalanceMapper balanceMapper, DepositMapper depositMapper) {
        this.movementService = movementService;
        this.balanceController = balanceController;
        this.balanceMapper = balanceMapper;
        this.depositMapper = depositMapper;
        this.walletService = walletService;
        this.balanceService = balanceService;
    }

    // Deposit

    @ResponseBody
    @Transactional(rollbackFor = Throwable.class)
    @PostMapping(path="/wallets/{id}/deposit")
    public ApiBalance deposit(@PathVariable("id") Long walletId, @RequestBody ApiDeposit payload) {
        Wallet wallet = getWallet(walletId);

        validateAmountPositive(payload.amount());
        validateCurrency(payload.currency(), wallet.currency());

        Movement depositMovement = new Movement(
                0L,
                MovementType.DEPOSIT,
                wallet.id(),
                payload.amount(),
                LocalDateTime.now(),
                payload.reference().orElse(""),
                0L
        );
        // Insert Movement
        Movement savedDeposit = movementService.insertMovement(depositMovement);
        // Update Balance
        Balance updatedBalance = balanceService.updateBalance(wallet.id(), payload.amount());
        return balanceMapper.modelToApi(updatedBalance);
    }




    // /wallets/id/deposit POST   (
    // Withdraw
    // Transfer
    // Balance in time

    private Wallet getWallet(Long walletId) {
        Optional<Wallet> maybeWallet = walletService.findWalletById(walletId);
        if(maybeWallet.isEmpty()){
            throw new WalletNotFoundException("Wallet with id " + walletId + " not found");
        }
        return maybeWallet.get();
    }

    private void validateAmountPositive(Double amount) {
        if (amount < 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }

    private void validateCurrency(String payloadCurrency, Currency walletCurrency) {
        Currency fromPayload = Currency.valueOf(payloadCurrency);
        if(!fromPayload.equals(walletCurrency)){
            throw new CurrencyMismatchException(String.format("Currency %s doesn't match wallet currency (%s)", payloadCurrency, walletCurrency.name()));
        }
    }

}
