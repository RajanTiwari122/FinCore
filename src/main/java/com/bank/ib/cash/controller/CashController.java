package com.bank.ib.cash.controller;

import com.bank.ib.cash.dto.CashFundingRequest;
import com.bank.ib.cash.service.CashPositionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cash")
public class CashController {

    private final CashPositionService cashService;

    public CashController(CashPositionService cashService) {
        this.cashService = cashService;
    }

    @PostMapping("/fund")
    @PreAuthorize("hasAnyRole('ADMIN','OPS')")
    public ResponseEntity<String> fundCash(@RequestBody CashFundingRequest request) {
            cashService.fundCash(
                    request.book,
                    request.currency,
                    request.amount
            );
            return ResponseEntity.ok("Successfully funded cash");
    }
}

