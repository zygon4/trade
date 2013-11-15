/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.trade.TradeMonitor;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.MarketConditions;

/**
 * 
 * @author zygon
 */
public interface TradeImpl {
    public void activate(MarketConditions marketConditions) throws ExchangeException;
    public void cancel() throws ExchangeException;
    /**
     * Returns the profit (or loss) after the final closing.
     * @param marketConditions
     * @return the profit (or loss) after the final closing.
     */
    public double close(MarketConditions marketConditions) throws ExchangeException;
    public String getDisplayIdentifier();
    public TradeMonitor getTradeMonitor();
    public Signal meetsEntryConditions(MarketConditions marketConditions);
    public Signal meetsExitConditions(MarketConditions marketConditions);
}
