package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.service.CurrencyService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/internal-api/currency")
public class InternalCurrencyController {

    private final CurrencyService currencyService;

    public InternalCurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    public void addCurrency(@RequestBody Map<String, Integer> pairs) {
        currencyService.addCurrency(pairs);
    }
}
