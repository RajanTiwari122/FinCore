package com.bank.ib.pnl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "unrealized_pnl")
@Getter @Setter
public class UnrealizedPnl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isin;
    private String book;

    private BigDecimal quantity;
    private BigDecimal costPrice;
    private BigDecimal marketPrice;

    private BigDecimal unrealizedPnl;

    private LocalDate valuationDate;
}

