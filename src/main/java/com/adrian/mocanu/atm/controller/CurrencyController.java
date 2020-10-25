package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.service.CurrencyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@Validated
public class CurrencyController {

    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping("/{amount}")
    @ResponseStatus
    public Map<String, Integer> addCurrency(@PathVariable Integer amount) {
        return currencyService.getCurrency(amount);
    }
}
