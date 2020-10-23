package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.service.CurrencyService;
import com.adrian.mocanu.atm.validator.SingleKeyHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/internal-api/currency")
@Validated
public class InternalCurrencyController {

    private final CurrencyService currencyService;

    public InternalCurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public List<CurrencyDb> addCurrency(@RequestBody @Size(min = 1, max = 100) SingleKeyHashMap pairs) {
        return currencyService.addCurrency(pairs);
    }
}
