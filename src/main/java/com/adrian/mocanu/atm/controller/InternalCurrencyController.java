package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.service.CurrencyManipulator;
import com.adrian.mocanu.atm.service.CurrencyService;
import com.adrian.mocanu.atm.model.ValidatedHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("/internal-api/currency")
@Validated
public class InternalCurrencyController {

	private final CurrencyManipulator currencyManipulator;

	public InternalCurrencyController(CurrencyService currencyManipulator) {
		this.currencyManipulator = currencyManipulator;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public List<CurrencyDb> addCurrency(
			@RequestBody @Size(min = 1, max = 100) ValidatedHashMap pairs) {
		return currencyManipulator.addCurrency(pairs);
	}

}
