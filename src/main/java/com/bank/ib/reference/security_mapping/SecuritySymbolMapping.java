package com.bank.ib.reference.security_mapping;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "security_symbol_mapping")
@Getter @Setter
public class SecuritySymbolMapping {

    @Id
    private String isin;

    @Column(nullable = false)
    private String yahooSymbol;
}

