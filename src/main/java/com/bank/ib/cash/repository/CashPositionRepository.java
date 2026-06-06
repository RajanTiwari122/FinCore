package com.bank.ib.cash.repository;

import com.bank.ib.cash.entity.CashPosition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CashPositionRepository
        extends JpaRepository<CashPosition, Long> {

    Optional<CashPosition> findByCurrencyAndBook(String currency, String book);
    List<CashPosition> findByBook(String book);
}

