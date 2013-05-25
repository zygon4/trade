/**
 * 
 */

package com.zygon.trade.strategy;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class TradeAgent2 {

    private final Logger logger = LoggerFactory.getLogger(TradeAgent2.class);
    private final Collection<Trade2> trades;
    private final TradeSummary tradeSummary = new TradeSummary();

    public TradeAgent2(Collection<Trade2> trades) {
        this.trades = trades;
    }
    
    public TradeSummary getTradeSummary() {
        return this.tradeSummary;
    }
    
    public void manageTrades() {
        this.logger.debug("Managing trades");
        
        
        for (Trade2 trade : this.trades) {
            switch (trade.getTradeState()) {
                case ACTIVE:
                    if (trade.canClose()) {
                        trade.closeTrade();
                    }
                    break;
                case OPEN:
                    if (trade.canActivate()) {
                        trade.activateTrade();
                        
                        // TODO: store monitor for the duration of the trade
                        // This is here just to demonstrate for now
                        TradeMonitor monitor = trade.getMonitor();
                    }
                    break;
                case CLOSED:
                    TradePostMortem postMortem = trade.reset();
                    
                    double profit = postMortem.getProfit();
                    long duration = postMortem.getDuration();
                    String durationDisplay = String.format("%d min, %d sec", 
                                TimeUnit.MILLISECONDS.toMinutes(duration),
                                TimeUnit.MILLISECONDS.toSeconds(duration) - 
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                            );
                    
                    this.logger.info("Trade lasted {} with a profit/loss of {}", durationDisplay, profit);
                    
                    if (profit >= 0.0) {
                        this.tradeSummary.addProfitableTrade();
                    } else {
                        this.tradeSummary.addLoosingTrade();
                    }
                    
                    this.logger.info("{}/{} win ratio", this.tradeSummary.getProfitableTrades(), 
                            this.tradeSummary.getLoosingTrades() + this.tradeSummary.getProfitableTrades());
                    
                    break;
            }
        }
    }
}
