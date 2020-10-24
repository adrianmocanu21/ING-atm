package com.adrian.mocanu.atm.controller;

import com.adrian.mocanu.atm.ApiTestSetup;
import com.adrian.mocanu.atm.TestUtils;
import com.adrian.mocanu.atm.repository.CurrencyRepository;
import com.adrian.mocanu.atm.service.CurrencyService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InternalCurrencyControllerTest extends ApiTestSetup {

    private final String PATH = "/internal-api/currency";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CurrencyService currencyService;

    @Value("classpath:duplicateKeyPostRequest.json")
    private Resource duplicateKeyPostJson;

    @Value("classpath:notConvertibleKeyPostRequest.json")
    private Resource notConvertibleKeyJson;

    @Value("classpath:exceededDenominationValue.json")
    private Resource exceededDenominationJson;

    @Autowired
    private CurrencyRepository currencyRepository;

    @BeforeEach
    void init() {
        currencyRepository.deleteAll();
    }

    @Test
    void controllerWorksFineOnValidRequest() throws Exception {
        var pairsMap = new HashMap<String, Integer>();
        TestUtils.generatePairs(pairsMap,3);
        var pairAsString = new Gson().toJson(pairsMap);

        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pairAsString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.[*].billDenomination").value(hasItems("1", "2", "3")))
                .andExpect(jsonPath("$.[*].numberOfBills").value(hasItems(1, 2, 3)));
    }

    @Test
    void errorIsThrownOnDuplicateKey() throws Exception {
        String duplicateKeyPost = TestUtils.convertResourceToString(duplicateKeyPostJson);

        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(duplicateKeyPost))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errorMessage").value("JSON parse error: Denomination 30 is duplicated; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Denomination 30 is duplicated (through reference chain: com.adrian.mocanu.atm.model.ValidatedHashMap[\"30\"])"));
    }

    @Test
    void errorIsThrownOnNonConvertibleKey() throws Exception {
        String notConvertibleKey = TestUtils.convertResourceToString(notConvertibleKeyJson);

        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(notConvertibleKey))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errorMessage").value("JSON parse error: Denomination notConvertibleKey is not of type integer; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Denomination notConvertibleKey is not of type integer (through reference chain: com.adrian.mocanu.atm.model.ValidatedHashMap[\"notConvertibleKey\"])"));
    }

    @Test
    void errorIsThrownOnExceededDenominationValue() throws Exception {
        String notConvertibleKey = TestUtils.convertResourceToString(exceededDenominationJson);

        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(notConvertibleKey))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("Bad Request"))
                .andExpect(jsonPath("$.errorMessage").value("JSON parse error: Denomination 1001 must be smaller than 1000; nested exception is com.fasterxml.jackson.databind.JsonMappingException: Denomination 1001 must be smaller than 1000 (through reference chain: com.adrian.mocanu.atm.model.ValidatedHashMap[\"1001\"])"));
    }

    @Test
    void errorIsThrownOnEmptyJson() throws Exception {
        String emptyJson = "{}";

        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(emptyJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("addCurrency.pairs: size must be between 1 and 100"));
    }

    @Test
    void errorIsThrownOnExceededSizeJson() throws Exception {
        var pairsMap = new HashMap<String, Integer>();
        TestUtils.generatePairs(pairsMap,101);
        var pairAsString = new Gson().toJson(pairsMap);

        this.mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pairAsString))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.title").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.errorMessage").value("addCurrency.pairs: size must be between 1 and 100"));
    }

}
