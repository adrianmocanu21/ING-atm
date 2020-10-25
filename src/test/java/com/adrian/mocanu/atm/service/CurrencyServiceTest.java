package com.adrian.mocanu.atm.service;

import com.adrian.mocanu.atm.TestUtils;
import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.repository.CurrencyQueryBuilder;
import com.adrian.mocanu.atm.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class CurrencyServiceTest {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CurrencyRepository currencyRepository;

    private Map<String, Integer> pairs = new HashMap<>();

    @BeforeEach
    void init() {
        pairs.clear();
        currencyRepository.deleteAll();
    }

    @Test
    void addCurrencyCreatesNewDenomination() {
        TestUtils.generatePairs(pairs, 4);
        var currencyList = currencyService.addCurrency(pairs);

        assertThat(currencyList.size()).isEqualTo(4);
        assertThat(currencyRepository.findByBillDenomination("3").get().getNumberOfBills()).isEqualTo(3);
    }

    @Test
    void addCurrencyUpdatesExistingDenomination() {
        currencyRepository.save(new CurrencyDb("2",2));
        TestUtils.generatePairs(pairs, 4);
        var currencyList = currencyService.addCurrency(pairs);

        assertThat(currencyList.size()).isEqualTo(4);
        assertThat(currencyRepository.findByBillDenomination("2").get().getNumberOfBills()).isEqualTo(4);
    }

    @Test
    void addCurrencyThrowsErrorWhenNumberOfBillsIsExceeded() {
        CurrencyQueryBuilder.setTotalNumberOfAcceptedBills(20);
        TestUtils.generatePairs(pairs, 30);

        Exception exception = assertThrows(RuntimeException.class, () -> currencyService.addCurrency(pairs));
        String expectedMessage = "The number of bills is exceeded! You are trying to add 445 more bills than expected";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
