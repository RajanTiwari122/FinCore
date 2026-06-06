package com.bank.ib.pnl.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "market_prices",
        uniqueConstraints = @UniqueConstraint(columnNames = {"isin", "price_date"}))
@Getter @Setter
public class MarketPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String isin;

    @Column(nullable = false)
    private LocalDate priceDate;

    @Column(nullable = false)
    private BigDecimal price;
}

