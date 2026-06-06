package com.bank.ib.exceptions;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String isin) {
        super("Price not found for ISIN: " + isin);
    }
}

