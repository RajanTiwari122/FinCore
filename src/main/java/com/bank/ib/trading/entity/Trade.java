package com.bank.ib.trading.entity;

import com.bank.ib.enums.TradeSide;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "trades")
@Getter @Setter
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tradeId;   // external / front-office id

    @Column(nullable = false)
    private String isin;

    @Column(nullable = false)
    private LocalDate tradeDate;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Column(nullable = false)
    private BigDecimal price;

    // 🔑 snapshot reference
    @Column(nullable = false)
    private Long securityVersionId;

    @Column(nullable = false)
    private String bookedBy;

    private Instant bookedAt = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeSide side;

    @Column(nullable = false)
    private BigDecimal cleanAmount;

    @Column(nullable = false)
    private BigDecimal accruedInterest;

    @Column(nullable = false)
    private BigDecimal settlementAmount;

}
