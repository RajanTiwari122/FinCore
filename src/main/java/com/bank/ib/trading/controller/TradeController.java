package com.bank.ib.trading.controller;

import com.bank.ib.trading.dto.TradeRequest;
import com.bank.ib.trading.dto.TradeResponse;
import com.bank.ib.trading.entity.Trade;
import com.bank.ib.trading.service.TradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private final TradeService tradeService;

    public TradeController(TradeService service) {
        this.tradeService = service;
    }

    // 🔐 Only TRADER can book trades
    @PostMapping("/bookTrade")
    @PreAuthorize("hasRole('TRADER')")
    public ResponseEntity<?> bookTrade(
            @RequestBody TradeRequest request,
            Authentication authentication) {
            Trade trade = tradeService.bookTrade(
                    request,
                    authentication.getName()
            );
            return ResponseEntity.ok(mapToResponse(trade));
    }

    /**
     * Get trade by tradeId
     */
    @GetMapping("/{tradeId}")
    @PreAuthorize("hasAnyRole('TRADER','RISK')")
    public TradeResponse getTrade(@PathVariable String tradeId) {

        Trade trade = tradeService.getByTradeId(tradeId);
        return mapToResponse(trade);
    }

    /**
     * List all trades (simple version)
     */
    @GetMapping("/allTrades")
    @PreAuthorize("hasAnyRole('TRADER','RISK')")
    public List<TradeResponse> getAllTrades() {

        return tradeService.getAllTrades()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TradeResponse mapToResponse(Trade trade) {
        TradeResponse r = new TradeResponse();
        r.id = trade.getId();
        r.tradeId = trade.getTradeId();
        r.isin = trade.getIsin();
        r.tradeDate = trade.getTradeDate();
        r.side = trade.getSide();
        r.quantity = trade.getQuantity();
        r.price = trade.getPrice();
        r.cleanAmount = trade.getCleanAmount();
        r.accruedInterest = trade.getAccruedInterest();
        r.settlementAmount = trade.getSettlementAmount();
        r.bookedBy = trade.getBookedBy();
        r.bookedAt = trade.getBookedAt();
        return r;
    }
}

