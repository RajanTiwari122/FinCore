package com.bank.ib.fund.repository;

import com.bank.ib.fund.entity.FundUnitBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundUnitRepository extends JpaRepository<FundUnitBalance, String> {

}
