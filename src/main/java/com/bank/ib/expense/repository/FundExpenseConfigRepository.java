package com.bank.ib.expense.repository;

import com.bank.ib.expense.entity.FundExpenseConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundExpenseConfigRepository extends JpaRepository<FundExpenseConfig, Long> {
    List<FundExpenseConfig> findByFundId(String fundId);
}
