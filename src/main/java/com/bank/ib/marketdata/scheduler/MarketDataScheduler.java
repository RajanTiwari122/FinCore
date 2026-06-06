package com.bank.ib.marketdata.scheduler;

import com.bank.ib.marketdata.service.MarketDataBatchService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class MarketDataScheduler {

    private final MarketDataBatchService batchService;

    public MarketDataScheduler(MarketDataBatchService batchService) {
        this.batchService = batchService;
    }

    /**
     * Runs every weekday at 6:30 PM IST
     */
    @Scheduled(cron = "0 30 18 ? * MON-FRI")
    public void fetchEodPrices() {

        LocalDate valuationDate =
                LocalDate.now(ZoneId.of("Asia/Kolkata"));

        batchService.fetchPricesForAll(valuationDate);
    }
}

