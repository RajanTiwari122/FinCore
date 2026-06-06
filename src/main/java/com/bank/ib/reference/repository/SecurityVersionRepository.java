package com.bank.ib.reference.repository;

import com.bank.ib.reference.entity.SecurityVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityVersionRepository
        extends JpaRepository<SecurityVersion, Long> {

    @Query("""
        SELECT v FROM SecurityVersion v
        WHERE v.security.isin = :isin
          AND v.effectiveFrom <= :asOf
          AND (v.effectiveTo IS NULL OR v.effectiveTo >= :asOf)
    """)
    List<SecurityVersion> findVersionAsOf(
            @Param("isin") String isin,
            @Param("asOf") LocalDate asOf);

    @Query("""
    SELECT v FROM SecurityVersion v
    WHERE v.security.isin = :isin
      AND v.active = true
    """)
    Optional<SecurityVersion> findActiveVersion(@Param("isin") String isin);

    @Query("""
    SELECT sv
    FROM SecurityVersion sv
    WHERE sv.security.isin = :isin
      AND sv.effectiveFrom <= :asOfDate
      AND (sv.effectiveTo IS NULL OR sv.effectiveTo > :asOfDate)
""")
    Optional<SecurityVersion> findBondVersionAsOf(
            String isin,
            LocalDate asOfDate
    );

}

