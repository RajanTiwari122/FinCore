package com.bank.ib.nav.repository;

import com.bank.ib.nav.entity.NavSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface NavRepository
        extends JpaRepository<NavSnapshot, Long> {

    void deleteByBookAndValuationDate(
            String book, LocalDate valuationDate);

    NavSnapshot findByBookAndValuationDate(String book, LocalDate valuationDate);
}

