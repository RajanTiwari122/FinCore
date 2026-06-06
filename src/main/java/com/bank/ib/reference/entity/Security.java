package com.bank.ib.reference.entity;

import com.bank.ib.enums.SecurityType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="securities")
@Getter @Setter
public class Security {
    @Column(nullable = false, unique = true)
    private String isin;
    @Id
    @Column(nullable = false, unique = true)
    private String security;
    private String description;
    @Enumerated(EnumType.STRING)
    private SecurityType type;
    @Column(nullable = false)
    private String currency;
    private boolean active = true;
}
