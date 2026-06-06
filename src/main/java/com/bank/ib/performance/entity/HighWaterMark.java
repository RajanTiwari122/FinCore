package com.bank.ib.performance.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "high_water_mark")
@Getter @Setter
public class HighWaterMark {

    @Id
    private String fundId;

    @Column(nullable = false)
    private BigDecimal hwmNavPerUnit;

    @Column(nullable = false)
    private LocalDate lastUpdated;
}

