package com.bank.ib.fund.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fund")
@Getter @Setter
public class Fund {

    @Id
    @Column(unique = true, nullable = false)
    private String code;   // e.g. FUND_EQ_01

    private String name;

    @Column(nullable = false)
    private String baseCurrency;
}

