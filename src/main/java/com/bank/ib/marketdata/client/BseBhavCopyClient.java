package com.bank.ib.marketdata.client;

import com.bank.ib.enums.SecurityType;
import com.bank.ib.pnl.entity.MarketPrice;
import com.bank.ib.reference.entity.Security;
import com.bank.ib.reference.entity.SecurityVersion;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class BseBhavCopyClient {

    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("ddMMyy");
    private final RestTemplate restTemplate;

    public BseBhavCopyClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<String, MarketPrice> fetchPrices(LocalDate date) {

        String dateStr = date.format(FORMAT);
        String url = String.format(
                "https://www.bseindia.com/download/BhavCopy/Equity/EQ%s_CSV.ZIP",
                dateStr
        );

        try {
            // Download ZIP
            InputStream zipStream = new URL(url).openStream();
            ZipInputStream zis = new ZipInputStream(zipStream);
            ZipEntry entry = zis.getNextEntry();

            if (entry == null) {
                throw new IllegalStateException("No CSV in Bhav Copy ZIP");
            }

            CSVParser parser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(new InputStreamReader(zis));

            Map<String, MarketPrice> result = new HashMap<>();

            for (CSVRecord r : parser) {
                MarketPrice p = new MarketPrice();
                p.setIsin(r.get("ISIN"));
                p.setPrice(new BigDecimal(r.get("CLOSE")));
                p.setPriceDate(date);

                result.put(p.getIsin(), p);
            }

            return result;

        } catch (Exception ex) {
            throw new RuntimeException("Failed to fetch BSE Bhav Copy", ex);
        }
    }

    public InputStream fetchBseCsv(LocalDate date) throws IOException {

        String yyyymmdd = date.format(DateTimeFormatter.BASIC_ISO_DATE);

        String url =
                "https://www.bseindia.com/download/BhavCopy/Equity/" +
                        "BhavCopy_BSE_CM_0_0_0_" + yyyymmdd + "_F_0000.CSV";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT,
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
        headers.set(HttpHeaders.ACCEPT,
                "text/csv,application/octet-stream");
        headers.set(HttpHeaders.REFERER,
                "https://www.bseindia.com/");
        headers.set(HttpHeaders.CONNECTION, "keep-alive");

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<byte[]> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.GET,
                        entity,
                        byte[].class
                );

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new IllegalStateException(
                    "BSE CSV not available: " + response.getStatusCode());
        }
        return new ByteArrayInputStream(response.getBody());
    }

    public Map<String, MarketPrice> parseCsv(InputStream is, LocalDate date)
            throws IOException {

        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new InputStreamReader(is, StandardCharsets.UTF_8));

        Map<String, MarketPrice> map = new HashMap<>();

        for (CSVRecord r : parser) {
            String isin = r.get("ISIN");
            if (isin == null || isin.isBlank()) continue;

            MarketPrice  p = new MarketPrice();
            p.setIsin(isin);
            p.setPrice(new BigDecimal(r.get("ClsPric")));
            p.setPriceDate(date);

            map.put(isin, p);
        }

        return map;
    }

    public  Map<Security, SecurityVersion> parseCsvToSecurity(InputStream is)
            throws IOException {

        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new InputStreamReader(is, StandardCharsets.UTF_8));

        Map<Security, SecurityVersion> map = new HashMap<>();

        for (CSVRecord r : parser) {
            String isin = r.get("ISIN");
            if (isin == null || isin.isBlank()) continue;

            Security s = new Security();
            s.setIsin(isin);
            s.setSecurity(r.get("TckrSymb"));
            s.setDescription(r.get("FinInstrmNm"));
            s.setActive(true);
            s.setCurrency("INR");
            s.setType(SecurityType.Equity);

            SecurityVersion sv = new SecurityVersion();
            sv.setSecurity(s);
            sv.setActive(true);
            sv.setCouponRate(BigDecimal.ZERO);
            sv.setFaceValue(BigDecimal.valueOf(100));
            sv.setEffectiveFrom(LocalDate.now());

            map.put(s, sv);
        }

        return map;
    }


}

