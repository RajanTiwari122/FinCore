package com.bank.ib.reference.dto;

import com.bank.ib.enums.SecurityType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SecurityResponse {
    public String isin;
    public String name;
    public SecurityType type;
    public String currency;
}
