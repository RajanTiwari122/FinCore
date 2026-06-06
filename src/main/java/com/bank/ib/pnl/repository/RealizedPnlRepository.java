package com.bank.ib.pnl.repository;

import com.bank.ib.pnl.entity.RealizedPnl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealizedPnlRepository
        extends JpaRepository<RealizedPnl, Long> {
}

