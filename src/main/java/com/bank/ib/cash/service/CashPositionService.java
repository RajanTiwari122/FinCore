package com.bank.ib.cash.service;

import com.bank.ib.cash.entity.CashPosition;
import com.bank.ib.cash.repository.CashPositionRepository;
import com.bank.ib.enums.TradeSide;
import com.bank.ib.trading.entity.Trade;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
public class CashPositionService {

    private final CashPositionRepository repo;

    public CashPositionService(CashPositionRepository repo) {
        this.repo = repo;
    }

    public void applyTrade(Trade trade, String currency) {

        CashPosition cash = repo
                .findByCurrencyAndBook(currency, trade.getBookedBy())
                .orElseGet(() -> {
                    CashPosition cp = new CashPosition();
                    cp.setCurrency(currency);
                    cp.setBook(trade.getBookedBy());
                    cp.setBalance(BigDecimal.ZERO);
                    return cp;
                });

        BigDecimal amount = trade.getSettlementAmount();

        if (trade.getSide() == TradeSide.BUY) {
            cash.setBalance(cash.getBalance().subtract(amount));
        } else {
            cash.setBalance(cash.getBalance().add(amount));
        }

        repo.save(cash);
    }

    @Transactional
    public void fundCash(String book, String currency, BigDecimal amount) {

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Funding amount must be positive");
        }

        CashPosition cash = repo
                .findByCurrencyAndBook(currency, book)
                .orElseGet(() -> {
                    CashPosition cp = new CashPosition();
                    cp.setCurrency(currency);
                    cp.setBook(book);
                    cp.setBalance(BigDecimal.ZERO);
                    return cp;
                });

        cash.setBalance(cash.getBalance().add(amount));
        repo.save(cash);
    }
}

