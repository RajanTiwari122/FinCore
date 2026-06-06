package com.bank.ib.reference.repository;

import com.bank.ib.reference.entity.BondAccruedInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BondAccruedInterestRepository extends JpaRepository<BondAccruedInterest, Long> {
}
