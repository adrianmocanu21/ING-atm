package com.adrian.mocanu.atm.repository;

import com.adrian.mocanu.atm.model.CurrencyDb;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CurrencyRepository extends MongoRepository<CurrencyDb, String> {

    Optional<CurrencyDb> findByBillDenomination(String billDenomination);

}
