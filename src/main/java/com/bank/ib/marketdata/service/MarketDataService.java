package com.bank.ib.marketdata.service;

import com.bank.ib.marketdata.client.BseBhavCopyClient;
import com.bank.ib.marketdata.client.YahooFinanceClient;
import com.bank.ib.marketdata.dto.MarketPriceResponse;
import com.bank.ib.marketdata.repository.SecuritySymbolMappingRepository;
import com.bank.ib.pnl.entity.MarketPrice;
import com.bank.ib.pnl.repository.MarketPriceRepository;
import com.bank.ib.reference.security_mapping.SecuritySymbolMapping;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
@Transactional
public class MarketDataService {

    private final YahooFinanceClient client;
    private final MarketPriceRepository priceRepo;
    private final SecuritySymbolMappingRepository mappingRepo;
    private final BseBhavCopyClient bseBhavCopyClient;

    public MarketDataService(
            YahooFinanceClient client,
            MarketPriceRepository priceRepo,
            SecuritySymbolMappingRepository mappingRepo,
            BseBhavCopyClient bseBhavCopyClient) {
        this.client = client;
        this.priceRepo = priceRepo;
        this.mappingRepo = mappingRepo;
        this.bseBhavCopyClient = bseBhavCopyClient;
    }

    public MarketPriceResponse fetchAndStorePrice(String isin, LocalDate priceDate) {

        SecuritySymbolMapping mapping =
                mappingRepo.findById(isin)
                        .orElseThrow(() ->
                                new IllegalStateException(
                                        "No Yahoo symbol mapped for ISIN " + isin));
        String ys = mapping.getYahooSymbol();

        BigDecimal price =
                client.fetchLastPrice(ys);

        MarketPrice mp =
                priceRepo.findByIsinAndPriceDate(isin, priceDate)
                        .orElseGet(MarketPrice::new);

        mp.setIsin(isin);
        mp.setPriceDate(priceDate);
        mp.setPrice(price);

         priceRepo.save(mp);
         return new MarketPriceResponse(isin,ys,priceDate,price);
    }

    public ResponseEntity<String> fetchBsePrices(LocalDate priceDate) {
        try{
            InputStream bseCsv = bseBhavCopyClient.fetchBseCsv(priceDate);
            Map<String,MarketPrice> bsePrices = bseBhavCopyClient.parseCsv(bseCsv,priceDate);
            priceRepo.saveAll(bsePrices.values());
            return ResponseEntity.ok("BSE Prices Fetched successfully");
        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}

