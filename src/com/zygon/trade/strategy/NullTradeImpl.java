/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.strategy.TradeImpl;
import com.zygon.trade.strategy.TradeMonitor;

/**
 *
 * @author zygon
 */
public class NullTradeImpl implements TradeImpl {

    @Override
    public void activate(MarketConditions marketConditions) throws ExchangeException {
        
    }

    @Override
    public double close(MarketConditions marketConditions) throws ExchangeException {
        return 0.0;
    }

    @Override
    public String getDisplayIdentifier() {
        return "NullTradeImpl";
    }

    @Override
    public void cancel() throws ExchangeException {
        
    }
    
    @Override
    public boolean meetsEntryConditions(MarketConditions marketConditions) {
        return false;
    }

    @Override
    public boolean meetsExitConditions(MarketConditions marketConditions) {
        return true;
    }

    @Override
    public TradeMonitor getTradeMonitor() {
        return new TradeMonitor();
    }
}
