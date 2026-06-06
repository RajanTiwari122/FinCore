package com.bank.ib.pnl.repository;

import com.bank.ib.pnl.entity.UnrealizedPnl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UnrealizedPnlRepository
        extends JpaRepository<UnrealizedPnl, Long> {

    void deleteByValuationDate(LocalDate valuationDate);
    List<UnrealizedPnl> findByBookAndValuationDate(String book,LocalDate valuationDate);
}

