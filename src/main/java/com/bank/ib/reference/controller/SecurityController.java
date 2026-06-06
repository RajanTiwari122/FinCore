package com.bank.ib.reference.controller;

import com.bank.ib.reference.dto.SecurityRequest;
import com.bank.ib.reference.dto.SecurityResponse;
import com.bank.ib.reference.dto.SecurityVersionRequest;
import com.bank.ib.reference.entity.Security;
import com.bank.ib.reference.entity.SecurityVersion;
import com.bank.ib.reference.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/securities")
public class SecurityController {

    private final SecurityService service;

    public SecurityController(SecurityService service) {
        this.service = service;
    }

    // 🔐 ADMIN only
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public SecurityResponse create(@RequestBody SecurityRequest request) {

        Security security = new Security();
        security.setIsin(request.isin);
        security.setSecurity(request.name);
        security.setType(request.type);
        security.setCurrency(request.currency);

        Security saved = service.create(security);

        SecurityResponse response = new SecurityResponse();
        response.isin = saved.getIsin();
        response.name = saved.getSecurity();
        response.type = saved.getType();
        response.currency = saved.getCurrency();

        return response;
    }

    // 🔐 ADMIN only
    @PostMapping("/create_multiple")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SecurityResponse> createMultiple(@RequestBody List<SecurityRequest> requestList) {
        List<Security> securityList = new ArrayList<>();
        requestList.forEach(request -> {
            Security security = new Security();
            security.setIsin(request.isin);
            security.setSecurity(request.name);
            security.setType(request.type);
            security.setCurrency(request.currency);
            securityList.add(security);
        });
        List<Security> savedList = service.createMultiple(securityList);
        List<SecurityResponse> responseList = new ArrayList<>();
        savedList.forEach(saved -> {
            SecurityResponse response = new SecurityResponse();
            response.isin = saved.getIsin();
            response.name = saved.getSecurity();
            response.type = saved.getType();
            response.currency = saved.getCurrency();
            responseList.add(response);
        });
        return responseList;
    }

    // 🔐 Read access to multiple roles
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN','TRADER','RISK','OPS')")
    public List<SecurityResponse> getAll() {

        return service.findAll()
                .stream()
                .map(s -> {
                    SecurityResponse r = new SecurityResponse();
                    r.isin = s.getIsin();
                    r.name = s.getSecurity();
                    r.type = s.getType();
                    r.currency = s.getCurrency();
                    return r;
                })
                .toList();
    }

    // 🔐 ADMIN only: create new version
    @PostMapping("/{isin}/versions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SecurityVersion> addVersion(@PathVariable String isin,
                                             @RequestBody SecurityVersionRequest req) {
        return ResponseEntity.ok(service.addVersion(isin, req));
    }

    // 🔐 ADMIN only: create new multiple versions
    @PostMapping("/add_versions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addMultipleVersions(@RequestBody List<SecurityVersionRequest> reqList) {
        for (SecurityVersionRequest req : reqList) {
            service.addVersion(req.isin, req);
        }
        return ResponseEntity.ok("All versions added successfully");
    }

    // 🔐 Read-only: resolve as-of date
    @GetMapping("/{isin}/version")
    @PreAuthorize("hasAnyRole('ADMIN','TRADER','RISK','OPS')")
    public ResponseEntity<SecurityVersion> getAsOf(@PathVariable String isin,
                                   @RequestParam LocalDate asOf) {
        return ResponseEntity.ok(service.getVersionAsOf(isin, asOf));
    }

    @PostMapping("/save_bse_securities/{asOf}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<SecurityVersion> saveBseSecurities(@PathVariable LocalDate asOf) {
        return service.saveBseSecurities();
    }
}

