package com.bank.ib.trading.repository;

import com.bank.ib.trading.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradeRepository extends JpaRepository<Trade, Long> {
    public Optional<Trade> findByTradeId(String tradeId);
}
