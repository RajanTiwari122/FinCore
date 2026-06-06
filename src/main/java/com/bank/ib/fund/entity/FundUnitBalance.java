package com.bank.ib.fund.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "fund_units")
@Getter
@Setter
public class FundUnitBalance {

    @Id
    private String fundId;

    @Column(nullable = false)
    private BigDecimal totalUnits;
}

