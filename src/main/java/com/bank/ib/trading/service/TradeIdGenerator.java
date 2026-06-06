package com.bank.ib.trading.service;

import com.bank.ib.trading.repository.TradeSequenceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TradeIdGenerator {

    private final TradeSequenceRepository repo;

    public TradeIdGenerator(TradeSequenceRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public String generateTradeId(LocalDate tradeDate) {

        Long seq = repo.getOrCreateAndLock(tradeDate);

        return String.format(
                "TRN%s-%04d",
                tradeDate.format(DateTimeFormatter.BASIC_ISO_DATE),
                seq
        );
    }

}

