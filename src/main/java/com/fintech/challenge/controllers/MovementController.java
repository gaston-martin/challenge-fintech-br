package com.fintech.challenge.controllers;

import com.fintech.challenge.api.ApiBalance;
import com.fintech.challenge.api.ApiDeposit;
import com.fintech.challenge.api.ApiMoneyTransfer;
import com.fintech.challenge.api.ApiWithdrawal;
import com.fintech.challenge.exceptions.client.CurrencyMismatchException;
import com.fintech.challenge.exceptions.client.InvalidAmountException;
import com.fintech.challenge.exceptions.client.InvalidTimeException;
import com.fintech.challenge.exceptions.client.WalletNotFoundException;
import com.fintech.challenge.mappers.BalanceMapper;
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
import java.time.format.DateTimeParseException;
import java.util.Optional;

@RestController
public class MovementController {

    private final MovementService movementService;
    private final WalletService walletService;
    private final BalanceService balanceService;
    private final BalanceMapper balanceMapper;

    @Autowired
    public MovementController(MovementService movementService, WalletService walletService, BalanceService balanceService, BalanceMapper balanceMapper) {
        this.movementService = movementService;
        this.balanceMapper = balanceMapper;
        this.walletService = walletService;
        this.balanceService = balanceService;
    }

    @ResponseBody
    @Transactional(rollbackFor = Throwable.class)
    @PostMapping(path="/wallets/{id}/deposit")
    public ApiBalance deposit(@PathVariable("id") Long walletId, @RequestBody ApiDeposit payload) {
        Wallet wallet = getWallet(walletId);

        validateAmountBePositive(payload.amount());
        validateCurrenciesMatch(payload.currency(), wallet.currency());

        Movement depositMovement = new Movement(
                0L,
                MovementType.DEPOSIT,
                wallet.id(),
                payload.amount(),
                LocalDateTime.now(),
                payload.reference().orElse("")
        );
        // Insert Movement
        Movement savedDeposit = movementService.insertMovement(depositMovement);
        // Update Balance
        Balance updatedBalance = balanceService.updateBalance(wallet.id(), payload.amount());
        return balanceMapper.modelToApi(updatedBalance);
    }

    @ResponseBody
    @Transactional(rollbackFor = Throwable.class)
    @PostMapping(path="/wallets/{id}/withdraw")
    public ApiBalance withdraw(@PathVariable("id") Long walletId, @RequestBody ApiWithdrawal payload) {
        Wallet wallet = getWallet(walletId);

        validateAmountBePositive(payload.amount());
        validateCurrenciesMatch(payload.currency(), wallet.currency());

        // Current savings before withdrawal are validated inside the balanceService.updateBalance method
        // So that when the balance doesn't have enough money for the withdrawal the exception thrown will
        // cause a rollback of the movement insertion. This could have been validated here before inserting the
        // movement, hence duplicating the validation. As the balance could be negatively modified by other
        // concurrent operation

        Movement withdrawMovement = new Movement(
                0L,
                MovementType.WITHDRAWAL,
                wallet.id(),
                payload.amount(),
                LocalDateTime.now(),
                payload.reference().orElse("")
        );

        // Insert Movement
        movementService.insertMovement(withdrawMovement);
        // Update Balance
        Double amountToSubtract = payload.amount() * (-1);
        Balance updatedBalance = balanceService.updateBalance(wallet.id(), amountToSubtract);
        return balanceMapper.modelToApi(updatedBalance);
    }

    @ResponseBody
    @Transactional(rollbackFor = Throwable.class)
    @PostMapping(path="/transfer")
    public String transfer(@RequestBody ApiMoneyTransfer payload){

        Wallet payerWallet = getWallet(payload.payerWalletId());
        Wallet collectorWallet = getWallet(payload.collectorWalletId());
        validateAmountBePositive(payload.amount());
        validateTransferCurrenciesMatch(payerWallet.currency(), collectorWallet.currency());
        validateCurrenciesMatch(payload.currency(), payerWallet.currency());

        LocalDateTime timeStamp = LocalDateTime.now();

        Movement payerMovement = new Movement(
                0L,
                MovementType.TRANSFER,
                payerWallet.id(),
                payload.amount() * (-1), // Subtract money
                timeStamp,
                payload.reference().orElse("")
        );
        Movement collectorMovement = new Movement(
                0L,
                MovementType.TRANSFER,
                collectorWallet.id(),
                payload.amount(),
                timeStamp,
                payload.reference().orElse("")
        );

        // Insert both movements
        Movement savedPayerMovement = movementService.insertMovement(payerMovement);
        Movement savedCollectorMovement = movementService.insertMovement(collectorMovement);

        // Update balances
        balanceService.updateBalance(payerWallet.id(), payerMovement.amount());
        balanceService.updateBalance(collectorWallet.id(), collectorMovement.amount());

        return "OK";
    }

    @ResponseBody
    @GetMapping(path="/wallets/{id}/balance/at/{time}")
    public ApiBalance getBalanceAtTime(@PathVariable("id") Long walletId, @PathVariable("time") String timeAsString) {
        // Validate if wallet exists
        Wallet wallet = getWallet(walletId);
        LocalDateTime time;
        try {
            time = LocalDateTime.parse(timeAsString);
        } catch (DateTimeParseException e){
            throw new InvalidTimeException(
                    String.format("Invalid time format (%s). Expected format: yyyy-MM-dd'T'HH:mm:ss.SSS", timeAsString));
        }

        Double amount = movementService.getBalanceAtTime(walletId, time);
        return new ApiBalance(
                wallet.id(),
                amount,
                wallet.currency().name()
        );
    }


    private Wallet getWallet(Long walletId) {
        Optional<Wallet> maybeWallet = walletService.findWalletById(walletId);
        if(maybeWallet.isEmpty()){
            throw new WalletNotFoundException("Wallet with id " + walletId + " not found");
        }
        return maybeWallet.get();
    }

    private void validateAmountBePositive(Double amount) {
        if (amount < 0) {
            throw new InvalidAmountException("Amount must be positive");
        }
    }

    private void validateCurrenciesMatch(String payloadCurrency, Currency walletCurrency) {
        Currency fromPayload = Currency.valueOf(payloadCurrency);
        if(!fromPayload.equals(walletCurrency)){
            throw new CurrencyMismatchException(String.format("Currency %s doesn't match wallet currency (%s)", payloadCurrency, walletCurrency.name()));
        }
    }

    private void validateTransferCurrenciesMatch(Currency leftCurrency, Currency rightCurrency) {
        if(!leftCurrency.equals(rightCurrency)){
            throw new CurrencyMismatchException(String.format("Payer currency (%s) doesn't match collector currency (%s)", leftCurrency.name(), rightCurrency.name()));
        }
    }
}
