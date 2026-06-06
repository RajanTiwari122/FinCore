package com.bank.ib.reference.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(
        name = "bond_coupon_schedule",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"isin", "coupon_date"}
        )
)
@Getter @Setter
public class BondCouponSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isin;
    private LocalDate couponDate;
}

