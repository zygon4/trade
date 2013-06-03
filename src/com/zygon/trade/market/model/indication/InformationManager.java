/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.zygon.trade.InformationHandler;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.strategy.TradeAgent;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the main entry point for incoming indications.
 * 
 * @author zygon
 * 
 */
public final class InformationManager implements InformationHandler<Object> {
    
    private final String name;
    private final Logger log;
    private final MarketConditions marketConditions;
    private final Collection<TradeAgent> tradeAgents;
    
    public InformationManager(String name, MarketConditions marketConditions, Collection<TradeAgent> tradeAgents) {
        this.name = name;
        this.marketConditions = marketConditions;
        this.tradeAgents = tradeAgents;
        this.log = LoggerFactory.getLogger(this.name);
    }
    
    public final Collection<TradeAgent> getTradeAgents() {
        return this.tradeAgents;
    }
    
    @Override
    public void handle(Object t) {
        this.log.trace("Handling " + t);
        
        Indication indication = (Indication) t;
        this.marketConditions.putIndication(indication, null); // TBD: wrap an indication with optional aggregation?
        
        // This is effectively where the data and strategy worlds collide.
        // This could also be done using a timer task style polling mechanism
        // for the traders to peek at the market conditions but this way 
        // forces action at every indication which is better for simulations.
        for (TradeAgent trader : this.tradeAgents) {
            trader.manageTrades();
        }
    }
    
    public void initialize() {
        for (TradeAgent trader : this.tradeAgents) {
            trader.set(this.marketConditions);
        }
    }
    
    public void uninitialize() {
        for (TradeAgent trader : this.tradeAgents) {
            trader.shutdown();
        }
    }
}
