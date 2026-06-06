package com.bank.ib.lot.repository;

import com.bank.ib.lot.entity.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LotRepository extends JpaRepository<Lot, Long> {

    List<Lot> findByIsinAndBookAndRemainingQuantityGreaterThanOrderByTradeDateAsc(
            String isin,
            String book,
            BigDecimal zero
    );
    List<Lot> findByRemainingQuantityGreaterThan(BigDecimal zero);
}

