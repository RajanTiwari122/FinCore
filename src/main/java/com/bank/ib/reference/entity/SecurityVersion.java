package com.bank.ib.reference.entity;

import com.bank.ib.enums.CouponFrequency;
import com.bank.ib.enums.DayCountConvention;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="security_versions")
@Getter @Setter
public class SecurityVersion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "security")
    private Security security;

    private BigDecimal couponRate;      // e.g. bonds
    private BigDecimal faceValue;

    @Column(nullable = false)
    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    @Column(nullable = false)
    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private CouponFrequency couponFrequency;

    @Enumerated(EnumType.STRING)
    private DayCountConvention dayCountConvention;
}
