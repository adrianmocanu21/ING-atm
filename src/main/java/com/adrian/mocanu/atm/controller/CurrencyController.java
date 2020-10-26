package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.service.CurrencyManipulator;
import com.adrian.mocanu.atm.service.CurrencyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/currency")
@Validated
public class CurrencyController {

	private final CurrencyManipulator currencyManipulator;

	public CurrencyController(CurrencyService currencyManipulator) {
		this.currencyManipulator = currencyManipulator;
	}

	@GetMapping("/{amount}")
	public Map<String, Integer> addCurrency(@PathVariable Integer amount) {
		return currencyManipulator.getCurrency(amount);
	}

}
