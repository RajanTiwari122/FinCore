package com.bank.ib.performance.repository;

import com.bank.ib.performance.entity.PerformanceFeeAccrual;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceFeeAccrualRepository extends JpaRepository<PerformanceFeeAccrual,String> {
}
