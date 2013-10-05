/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.execution.MarketConditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class TradeAgent {

    private final Logger logger = LoggerFactory.getLogger(TradeAgent.class);
    private final String name;
    private final Collection<Trade> trades;
    private final TradeSummary tradeSummary;
    
    private final Object shutdownLock = new Object();
    private volatile boolean shutdown = false;
    
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
            String entrySignal = postMortem.getEntrySignal().getName();
            String exitSignal = postMortem.getExitSignal().getName();

            logger.info("{}, {}, {}, {}", durationDisplay, profit, entrySignal, exitSignal);
        }
    };

    public TradeAgent(String name, Collection<Trade> trades) {
        this.name = name;
        this.trades = trades;
        this.tradeSummary = new TradeSummary(name);
    }
    
    public TradeSummary getAgentSummary() {
        return this.tradeSummary;
    }

    public String getName() {
        return this.name;
    }
    
    public void getTradeState(StringBuilder sb) {
        for (Trade trade : this.trades) {
            sb.append(trade.getTradeStatusString()).append('\n');
        }
    }
    
    public TradeSummary[] getTradeSummary() {
        
        List<TradeSummary> summaries = new ArrayList<>();
        
        for (Trade trade : this.trades) {
            summaries.add(trade.getTradeSummary());
        }
        
        return summaries.toArray(new TradeSummary[summaries.size()]);
    }
    
    public void manageTrades() {
        
        synchronized (this.shutdownLock) {
            if (!this.shutdown) {
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
            } else {
                this.logger.debug("Shutdown - not managing trades");
            }
        }
    }

    public final void set(MarketConditions marketConditions) {
        for (Trade trade : this.trades) {
            trade.set(marketConditions);
        }
    }

    public void shutdown() {
        this.shutdown = true;
        
        for (Trade trade : this.trades) {
            trade.cancel();
        }
    }
}
