package com.fintech.challenge.controllers;

import com.fintech.challenge.util.Currency;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/currencies")
public class CurrencyController {

    @GetMapping
    @ResponseBody
    public List<String> getKnownCurrencies(){

        return Stream
                .of(Currency.values())
                .map(Currency::name)
                .sorted()
                .toList();
    }
}
