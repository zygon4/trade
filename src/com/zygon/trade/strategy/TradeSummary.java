/**
 * 
 */

package com.zygon.trade.strategy;

/**
 *
 * This would be nice as a more robust statistics mechanism vs adhoc summaries.
 * 
 * @author zygon
 */
public class TradeSummary {

    private int profitableTrades = 0;
    private int loosingTrades = 0;
    
    public synchronized void addProfitableTrade() {
        this.profitableTrades++;
    }
    
    public synchronized void addLoosingTrade() {
        this.loosingTrades++;
    }

    public int getLoosingTrades() {
        return this.loosingTrades;
    }

    public int getProfitableTrades() {
        return this.profitableTrades;
    }
}
