package com.bank.ib.performance.repository;

import com.bank.ib.performance.entity.PerformanceFeeConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceFeeConfigRepository extends JpaRepository<PerformanceFeeConfig, String> {
}
