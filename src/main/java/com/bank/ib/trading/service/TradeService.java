package com.bank.ib.trading.service;

import com.bank.ib.cash.service.CashPositionService;
import com.bank.ib.enums.TradeSide;
import com.bank.ib.lot.model.LotConsumption;
import com.bank.ib.lot.service.LotService;
import com.bank.ib.pnl.service.RealizedPnlService;
import com.bank.ib.position.service.PositionService;
import com.bank.ib.reference.entity.SecurityVersion;
import com.bank.ib.reference.service.SecurityService;
import com.bank.ib.trading.dto.TradeRequest;
import com.bank.ib.trading.dto.TradeResponse;
import com.bank.ib.trading.economics.TradeEconomics;
import com.bank.ib.trading.economics.TradeEconomicsResolver;
import com.bank.ib.trading.economics.TradeEconomicsResult;
import com.bank.ib.trading.entity.Trade;
import com.bank.ib.trading.repository.TradeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TradeService {

    private final TradeRepository tradeRepo;
    private final SecurityService securityService;
    private final TradeEconomicsResolver economicsResolver;
    private final CashPositionService cashService;
    private final PositionService positionService;
    private final TradeIdGenerator tradeIdGenerator;
    private final LotService lotService;
    private final RealizedPnlService realizedPnlService;


    public TradeService(TradeRepository tradeRepo,
                        SecurityService securityService
                        , TradeEconomicsResolver economicsResolver,
                        CashPositionService cashService,
                        PositionService positionService,
                        TradeIdGenerator tradeIdGenerator,
                        LotService lotService,
                        RealizedPnlService realizedPnlService) {
        this.tradeRepo = tradeRepo;
        this.securityService = securityService;
        this.economicsResolver = economicsResolver;
        this.cashService = cashService;
        this.positionService = positionService;
        this.tradeIdGenerator = tradeIdGenerator;
        this.lotService = lotService;
        this.realizedPnlService = realizedPnlService;
    }

    public Trade bookTrade(TradeRequest req, String username) {

        // 🔑 Resolve correct security version AS-OF trade date
        SecurityVersion version =
                securityService.getVersionAsOf(req.isin, req.tradeDate);

        TradeEconomics economics =
                economicsResolver.resolve(
                        version.getSecurity().getType());

        Trade trade = new Trade();
        trade.setTradeId(tradeIdGenerator.generateTradeId(req.tradeDate));
        trade.setIsin(req.isin);
        trade.setTradeDate(req.tradeDate);
        trade.setQuantity(req.quantity);
        trade.setPrice(req.price);
        trade.setSecurityVersionId(version.getId());
        trade.setBookedBy(username);
        trade.setSide(req.side);

        TradeEconomicsResult econResult =
                economics.calculate(trade, version);

        trade.setCleanAmount(econResult.getCleanAmount());
        trade.setAccruedInterest(econResult.getAccruedInterest());
        trade.setSettlementAmount(econResult.getSettlementAmount());

        Trade saved = tradeRepo.save(trade);

        if(TradeSide.BUY == trade.getSide()) {
            lotService.createLot(saved);
        }else{
            // 1️⃣ consume lots
            List<LotConsumption> consumptions =
                    lotService.consumeLots(saved);

            // 2️⃣ book realized P&L
            realizedPnlService.bookRealizedPnl(saved, consumptions);
        }

    // 3️⃣ update position
        positionService.applyTrade(saved);

    // 4️⃣ update cash
        cashService.applyTrade(saved, version.getSecurity().getCurrency());
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Trade> getAllTrades() {
        return tradeRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Trade getByTradeId(String tradeId) {
        return tradeRepo.findByTradeId(tradeId).orElse(null);
    }
}

