package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.service.CurrencyService;
import com.adrian.mocanu.atm.validator.SingleKeyHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;

@RestController
@RequestMapping("/internal-api/currency")
@Validated
public class InternalCurrencyController {

    private final CurrencyService currencyService;

    public InternalCurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    public ResponseEntity addCurrency(@RequestBody @Size(min = 1, max = 100) SingleKeyHashMap pairs) {
        currencyService.addCurrency(pairs);
        return new ResponseEntity(HttpStatus.OK);
    }
}
