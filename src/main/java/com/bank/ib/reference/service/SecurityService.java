package com.bank.ib.reference.service;

import com.bank.ib.marketdata.client.BseBhavCopyClient;
import com.bank.ib.reference.dto.SecurityResponse;
import com.bank.ib.reference.dto.SecurityVersionRequest;
import com.bank.ib.reference.entity.Security;
import com.bank.ib.reference.entity.SecurityVersion;
import com.bank.ib.reference.repository.SecurityRepository;
import com.bank.ib.reference.repository.SecurityVersionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SecurityService {
    private final SecurityRepository repository;
    private final SecurityVersionRepository versionRepository;
    private final BseBhavCopyClient bsebhavCopyClient;

    public SecurityService(SecurityRepository repository,SecurityVersionRepository versionRepository,
                           BseBhavCopyClient bsebhavCopyClient) {
        this.repository = repository;
        this.versionRepository = versionRepository;
        this.bsebhavCopyClient = bsebhavCopyClient;
    }

    public Security create(Security security) {
        repository.findByIsin(security.getIsin())
                .ifPresent(s -> {
                    throw new IllegalStateException("Security already exists");
                });

        return repository.save(security);
    }
    public List<Security> createMultiple(List<Security> securityList) {
        List<Security> finalList = new ArrayList<>();
        securityList.forEach(security -> {
            if(repository.findByIsin(security.getIsin()).isEmpty()) {
               finalList.add(security);
            };
        });
        return repository.saveAll(finalList);
    }

    @Transactional(readOnly = true)
    public List<Security> findAll() {
        return repository.findAll();
    }

    // ADMIN operations
    public SecurityVersion addVersion(String isin,
                                      SecurityVersionRequest req) {

        Security security = repository.findByIsin(isin)
                .orElseThrow(() -> new IllegalStateException("Security not found"));

        // 🔒 Close currently active version
        versionRepository.findActiveVersion(isin)
                .ifPresent(existing -> {
                    if (!existing.getEffectiveFrom().isBefore(req.effectiveFrom)) {
                        throw new IllegalStateException(
                                "New version must start after current version");
                    }
                    existing.setEffectiveTo(req.effectiveFrom.minusDays(1));
                    existing.setActive(false);
                });

        SecurityVersion version = new SecurityVersion();
        version.setSecurity(security);
        version.setCouponRate(req.couponRate);
        version.setFaceValue(req.faceValue);
        version.setEffectiveFrom(req.effectiveFrom);
        version.setActive(true);

        return versionRepository.save(version);
    }

    @Transactional(readOnly = true)
    public SecurityVersion getVersionAsOf(String isin, LocalDate asOf) {

        List<SecurityVersion> versions =
                versionRepository.findVersionAsOf(isin, asOf);

        if (versions.isEmpty()) {
            throw new IllegalStateException("No version found for date");
        }

        if (versions.size() > 1) {
            throw new IllegalStateException(
                    "Data integrity error: multiple versions active for date");
        }

        return versions.get(0);
    }

    public List<SecurityVersion> saveBseSecurities(){
        LocalDate now = LocalDate.now().minusDays(2);
        try{
            InputStream is = bsebhavCopyClient.fetchBseCsv(now);
            Map<Security, SecurityVersion> result = bsebhavCopyClient.parseCsvToSecurity(is);
            repository.saveAll(result.keySet());
            return versionRepository.saveAll(result.values());

        }catch(IOException ex){
            throw new RuntimeException(ex);
        }


    }


}
