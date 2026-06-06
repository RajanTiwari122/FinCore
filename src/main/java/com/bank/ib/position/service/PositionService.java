package com.bank.ib.position.service;

import com.bank.ib.enums.TradeSide;
import com.bank.ib.position.entity.Position;
import com.bank.ib.position.repository.PositionRepository;
import com.bank.ib.trading.entity.Trade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional
public class PositionService {

    private final PositionRepository repo;

    public PositionService(PositionRepository repo) {
        this.repo = repo;
    }

    public void applyTrade(Trade trade) {

        Position position = repo
                .findByIsinAndBook(trade.getIsin(), trade.getBookedBy())
                .orElseGet(() -> {
                    Position p = new Position();
                    p.setIsin(trade.getIsin());
                    p.setBook(trade.getBookedBy());
                    p.setQuantity(BigDecimal.ZERO);
                    return p;
                });

        if (trade.getSide() == TradeSide.SELL) {
            if (position.getQuantity().compareTo(trade.getQuantity()) < 0) {
                throw new IllegalStateException(
                        "Insufficient position for SELL trade");
            }
            position.setQuantity(
                    position.getQuantity().subtract(trade.getQuantity()));
        } else {
            position.setQuantity(
                    position.getQuantity().add(trade.getQuantity()));
        }

        repo.save(position);
    }
}

