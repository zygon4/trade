/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.execution.MarketConditions;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class TradeAgent {

    private final Logger logger = LoggerFactory.getLogger(TradeAgent.class);
    private final Collection<Trade> trades;
    private final TradeSummary tradeSummary = new TradeSummary();
    
    // TODO: implement persistent logger
    private final TradeLogger tradeLogger = new TradeLogger() {

        @Override
        public void log(TradePostMortem postMortem) {
            double profit = postMortem.getProfit();
            long duration = postMortem.getDuration();
            String durationDisplay = String.format("%d min, %d sec", 
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) - 
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
                    );

            logger.info("Trade lasted {} with a profit/loss of {}", durationDisplay, profit);
        }
    };

    public TradeAgent(Collection<Trade> trades) {
        this.trades = trades;
    }
    
    public TradeSummary getTradeSummary() {
        return this.tradeSummary;
    }
    
    public void manageTrades() {
        this.logger.debug("Managing trades");
        
        for (Trade trade : this.trades) {
            switch (trade.getTradeState()) {
                case ACTIVE:
                    if (trade.canClose()) {
                        trade.closeTrade();
                    } else if (trade.canCancel()) {
                        trade.cancel();
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
            }
            
            // Performing cleanup outside the switch because we want to prep the
            // trade for the next activation inline without waiting for another
            // manage cycle.
            if (trade.getTradeState() == Trade.TradeState.CLOSED) {
                TradePostMortem postMortem = trade.reset();
                this.tradeLogger.log(postMortem);
                
                this.logger.info("Trade {}: {}", trade.getDisplayIdentifier(), trade.getTradeSummary().getSummaryStmt());

                this.tradeSummary.add(postMortem.getProfit());

                this.logger.info("Overall: {}", this.tradeSummary.getSummaryStmt());
            }
        }
    }

    public final void set(MarketConditions marketConditions) {
        for (Trade trade : this.trades) {
            trade.set(marketConditions);
        }
    }
}
