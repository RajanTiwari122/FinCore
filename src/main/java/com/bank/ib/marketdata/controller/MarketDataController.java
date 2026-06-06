package com.bank.ib.marketdata.controller;

import com.bank.ib.marketdata.service.MarketDataBatchService;
import com.bank.ib.marketdata.service.MarketDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/market-data")
public class MarketDataController {

    private final MarketDataService service;
    private final MarketDataBatchService batchService;

    public MarketDataController(MarketDataService service, MarketDataBatchService batchService) {
        this.service = service;
        this.batchService = batchService;
    }

    @PostMapping("/fetch/{isin}")
    @PreAuthorize("hasRole('OPS')")
    public ResponseEntity<?> fetchPrice(
            @PathVariable String isin,
            @RequestParam LocalDate date) {
            return ResponseEntity.ok(service.fetchAndStorePrice(isin, date));
    }

    @PostMapping("store_price/{pricingDate}")
    @PreAuthorize("hasAnyRole('OPS','ADMIN')")
    public ResponseEntity<String> fetchPriceForMultipleSecurities(
            @PathVariable LocalDate pricingDate) {
        return service.fetchBsePrices(pricingDate);
    }
}

