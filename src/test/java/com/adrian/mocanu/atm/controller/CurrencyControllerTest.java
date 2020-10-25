package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.TestUtils;
import com.adrian.mocanu.atm.model.CurrencyDb;
import com.adrian.mocanu.atm.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyControllerTest {

    private final String PATH = "/api/currency";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void cleanRepository() {
        currencyRepository.deleteAll();
    }

    @Test
    void getCurrencyReturnsDesiredAmount() throws Exception {
        var desiredAmount = 37;
        var currencyDbs = TestUtils.generateCurrencyDbs(5);
        currencyRepository.saveAll(currencyDbs);
        this.mockMvc.perform(get(PATH + "/" + desiredAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCurrencyReturnsDesiredAmountAndUpdatesDb() throws Exception {
        var desiredAmount = 5;
        var currencyDbs = TestUtils.generateCurrencyDbs(2);
        currencyRepository.saveAll(currencyDbs);

        this.mockMvc.perform(get(PATH + "/" + desiredAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.1").value(1))
                .andExpect(jsonPath("$.2").value(2));

        assertThat(currencyRepository.findByBillDenomination("1")).isEqualTo(Optional.empty());
        assertThat(currencyRepository.findByBillDenomination("2")).isEqualTo(Optional.empty());

    }

    @Test
    void getCurrencyThrowsExceptionWhenTheAmountIsExceeded() throws Exception {
        var desiredAmount = 7;
        var currencyDbs = TestUtils.generateCurrencyDbs(2);
        currencyRepository.saveAll(currencyDbs);

        this.mockMvc.perform(get(PATH + "/" + desiredAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("We do not have this amount! Try as smaller value"));

    }

    @Test
    void getCurrencyThrowsExceptionWhenTheAtmIsEmpty() throws Exception {
        var desiredAmount = 7;

        this.mockMvc.perform(get(PATH + "/" + desiredAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("The ATM is out of cash! Please try later"));

    }

    @Test
    void getCurrencyThrowsExceptionWhenAtmHasTheDesiredAmountButNoSuitableBills() throws Exception {
        var desiredAmount = 7;
        var currency = new CurrencyDb("5",2);
        currencyRepository.save(currency);

        this.mockMvc.perform(get(PATH + "/" + desiredAmount)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("We do not have this amount! Try as smaller value"));
        assertThat(currencyRepository.findByBillDenomination("5").get().getNumberOfBills()).isEqualTo(2);
    }
}
