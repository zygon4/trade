/**
 * 
 */

package com.zygon.trade.strategy;

import java.util.Collection;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a floor of trading agents which will be called upon to act on
 * incoming information.
 *
 * @author zygon
 */
public class TradingFloor {
    
    private final Logger logger = LoggerFactory.getLogger(TradingFloor.class);
    private final Collection<TradeAgent> traders;

    public TradingFloor(Collection<TradeAgent> traders) {
        this.traders = Collections.unmodifiableCollection(traders);
    }
    
    public void handle(IndicationProcessor.Response response) {
        this.logger.debug("Handling: " + response);
        
        // First let them clear out any trades they may have
        for (TradeAgent trader : this.traders) {
            trader.manageActiveTrades();
        }
        
        // Then process the current information
        for (TradeAgent trader : this.traders) {
            trader.processInformation(response);
        }
        
        // Finally work any new trades
        for (TradeAgent trader : this.traders) {
            trader.generateNewTrades();
        }
    }
}
