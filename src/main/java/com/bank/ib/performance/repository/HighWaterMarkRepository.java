package com.bank.ib.performance.repository;


import com.bank.ib.performance.entity.HighWaterMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HighWaterMarkRepository extends JpaRepository<HighWaterMark,String> {
}
