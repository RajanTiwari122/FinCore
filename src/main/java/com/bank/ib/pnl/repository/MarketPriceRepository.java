package com.bank.ib.pnl.repository;

import com.bank.ib.pnl.entity.MarketPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface MarketPriceRepository
        extends JpaRepository<MarketPrice, Long> {

    Optional<MarketPrice>
    findByIsinAndPriceDate(String isin, LocalDate priceDate);
}

