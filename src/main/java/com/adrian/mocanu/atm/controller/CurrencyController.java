package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.service.CurrencyRetrieveManipulator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@Validated
public class CurrencyController {

	private final CurrencyRetrieveManipulator currencyRetrieveManipulator;

	public CurrencyController(CurrencyRetrieveManipulator currencyRetrieveManipulator) {
		this.currencyRetrieveManipulator = currencyRetrieveManipulator;
	}

	@GetMapping("/{amount}")
	public Map<String, Integer> addCurrency(@PathVariable Integer amount) {
		return currencyRetrieveManipulator.getCurrency(amount);
	}

}
