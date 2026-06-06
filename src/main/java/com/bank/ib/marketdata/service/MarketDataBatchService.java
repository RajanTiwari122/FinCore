package com.bank.ib.marketdata.service;

import com.bank.ib.marketdata.client.YahooFinanceClient;
import com.bank.ib.marketdata.repository.SecuritySymbolMappingRepository;
import com.bank.ib.pnl.entity.MarketPrice;
import com.bank.ib.pnl.repository.MarketPriceRepository;
import com.bank.ib.reference.entity.Security;
import com.bank.ib.reference.repository.SecurityRepository;
import com.bank.ib.reference.security_mapping.SecuritySymbolMapping;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class MarketDataBatchService {

    private final SecurityRepository securityRepo;
    private final SecuritySymbolMappingRepository mappingRepo;
    private final YahooFinanceClient yahooClient;
    private final MarketPriceRepository priceRepo;

    public MarketDataBatchService(
            SecurityRepository securityRepo,
            SecuritySymbolMappingRepository mappingRepo,
            YahooFinanceClient yahooClient,
            MarketPriceRepository priceRepo) {
        this.securityRepo = securityRepo;
        this.mappingRepo = mappingRepo;
        this.yahooClient = yahooClient;
        this.priceRepo = priceRepo;
    }

    public void fetchPricesForAll(LocalDate priceDate) {

        List<Security> securities =
                securityRepo.findByActiveTrue();

        List<String> isinList = securities.stream().map(Security::getIsin).toList();

        List<SecuritySymbolMapping> securityMappingList = mappingRepo.findByIsins(isinList);
        Map<String,String> isinSymbolMap = securityMappingList.stream().collect(Collectors.toMap(
                SecuritySymbolMapping::getYahooSymbol,
                SecuritySymbolMapping::getIsin,
                (existing, replacement) -> existing));

        String symbols = String.join(",",isinSymbolMap.keySet());

        Map<String, BigDecimal> prices =
                yahooClient.fetchPeriodicPrices(
                        symbols,priceDate,priceDate,"1d");

        for (String sec : prices.keySet()) {
          String[] split  = sec.split("\\|");
          String isin = isinSymbolMap.get(split[0]);
          LocalDate date = LocalDate.parse(split[1]);
          BigDecimal price = prices.get(sec);

          MarketPrice mp =
                priceRepo.findByIsinAndPriceDate(isin, date)
                        .orElseGet(MarketPrice::new);

                mp.setIsin(isin);
                mp.setPriceDate(date);
                mp.setPrice(price);

                priceRepo.save(mp);

        }
    }
}

