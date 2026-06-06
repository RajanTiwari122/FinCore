package com.bank.ib.reference.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class SecurityVersionRequest {
    public String isin;
    public BigDecimal couponRate;
    public BigDecimal faceValue;
    public LocalDate effectiveFrom;
}

