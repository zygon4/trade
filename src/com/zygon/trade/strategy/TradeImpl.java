/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.MarketConditions;

/**
 * Consider an abstract TradeImpl to handle basic things such as price/loss stops
 * 
 * @author zygon
 */
public interface TradeImpl {
    public void activate(MarketConditions marketConditions) throws ExchangeException;
    /**
     * Returns the profit (or loss) after the final closing.
     * @param marketConditions
     * @return the profit (or loss) after the final closing.
     */
    public double close(MarketConditions marketConditions) throws ExchangeException;
    public String getDisplayIdentifier();
    public TradeMonitor getTradeMonitor();
    public boolean meetsEntryConditions(MarketConditions marketConditions);
    public boolean meetsExitConditions(MarketConditions marketConditions);
}
