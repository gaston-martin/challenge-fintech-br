package com.fintech.challenge.controllers;

import com.fintech.challenge.util.Country;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping(path = "/countries")
public class CountryController {

    @GetMapping
    @ResponseBody
    public List<String> getKnownCountries(){

        return Stream
                .of(Country.values())
                .map(Country::name)
                .sorted()
                .toList();
    }
}
