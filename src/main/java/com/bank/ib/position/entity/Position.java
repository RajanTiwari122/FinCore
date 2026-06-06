package com.bank.ib.position.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "positions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"isin", "book"}))
@Getter @Setter
public class Position {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isin;

    private String book;   // trader / desk / account

    @Column(nullable = false)
    private BigDecimal quantity;

    // getters & setters
}

