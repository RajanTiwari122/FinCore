package com.bank.ib.fund.repository;

import com.bank.ib.fund.entity.FundNavSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface FundNavSnapshotRepository extends JpaRepository<FundNavSnapshot, String> {
    FundNavSnapshot findByFundIdAndValuationDate(String fundId, LocalDate valuationDate);
}
