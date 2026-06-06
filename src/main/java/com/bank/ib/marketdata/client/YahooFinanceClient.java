package com.bank.ib.marketdata.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class YahooFinanceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal fetchLastPrice(String yahooSymbol) {

        String url =
                "https://query1.finance.yahoo.com/v7/finance/quote?symbols="
                        + yahooSymbol;

        Map response = restTemplate.getForObject(url, Map.class);

        Map quote =
                (Map) ((List) ((Map) response.get("quoteResponse"))
                        .get("result"))
                        .get(0);

        Object price = quote.get("regularMarketPrice");

        if (price == null) {
            throw new IllegalStateException(
                    "Price not available for " + yahooSymbol);
        }

        return new BigDecimal(price.toString());
    }

    public Map<String, BigDecimal> fetchPeriodicPrices(
            String yahooSymbol,
            LocalDate startDate,
            LocalDate endDate,
            String interval // 1d, 1wk, 1mo
    ) {

        long period1 = startDate.atStartOfDay(ZoneId.of("UTC")).toEpochSecond();
        long period2 = endDate.plusDays(1)
                .atStartOfDay(ZoneId.of("UTC"))
                .toEpochSecond();

        String url = String.format(
                "https://query1.finance.yahoo.com/v8/finance/chart/%s"
                        + "?period1=%d&period2=%d&interval=%s&events=history&includeAdjustedClose=true",
                yahooSymbol, period1, period2, interval
        );
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        Map<String, Object> chart = (Map<String, Object>) response.get("chart");
        List<Map<String, Object>> result =
                (List<Map<String, Object>>) chart.get("result");

        if (result == null || result.isEmpty()) {
            throw new IllegalStateException("No data returned for " + yahooSymbol);
        }

        Map<String, Object> data = result.get(0);
        String symbol = (String) ((Map<String, Object>) data.get("meta")).get("symbol");

        List<Integer> timestamps = (List<Integer>) data.get("timestamp");
        Map<String, Object> indicators = (Map<String, Object>) data.get("indicators");
        List<Map<String, Object>> quotes =
                (List<Map<String, Object>>) indicators.get("quote");

        List<Number> closes = (List<Number>) quotes.get(0).get("close");

        Map<String, BigDecimal> prices = new LinkedHashMap<>();

        for (int i = 0; i < timestamps.size(); i++) {
            if (closes.get(i) == null) continue; // holidays / missing data

            LocalDate date = Instant.ofEpochSecond(timestamps.get(i))
                    .atZone(ZoneId.of("UTC"))
                    .toLocalDate();

            prices.put(symbol+"|"+ date, BigDecimal.valueOf(closes.get(i).doubleValue()));
        }
        return prices;
    }

}

