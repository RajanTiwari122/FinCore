package com.bank.ib.performance.entity;

import com.bank.ib.enums.PerformanceFeeFrequency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "performance_fee_config")
@Getter @Setter
public class PerformanceFeeConfig {

    @Id
    private String fundId;

    // e.g. 0.20 = 20%
    @Column(nullable = false)
    private BigDecimal performanceFeeRate;

    // monthly, quarterly, etc (extend later)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PerformanceFeeFrequency frequency;
}

