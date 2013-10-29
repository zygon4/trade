/**
 * 
 */

package com.zygon.trade.strategy.impl;

import com.zygon.trade.agent.TradeSignal;
import com.zygon.trade.execution.MarketConditions;

/**
 *
 * @author zygon
 */
public interface Strategy {
    public String getDisplayIdentifier();
    
    public TradeSignal getEntrySignal(MarketConditions marketConditions);
    public TradeSignal getExitSignal(MarketConditions marketConditions);
    
    
}
