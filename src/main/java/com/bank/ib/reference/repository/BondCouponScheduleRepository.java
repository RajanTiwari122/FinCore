package com.bank.ib.reference.repository;

import com.bank.ib.reference.entity.BondCouponSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BondCouponScheduleRepository extends JpaRepository<BondCouponSchedule, Long> {
    @Query("""
    SELECT  bcs.couponDate FROM BondCouponSchedule bcs WHERE bcs.couponDate <= :couponDate AND bcs.isin = :isin""")
    public LocalDate findLastCouponDate(String isin, LocalDate couponDate);
}
