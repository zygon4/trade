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
    
    public void add(TradeSummary summary) {
        this.addProfitableTrades(summary.getProfitableTrades());
        this.addLoosingTrades(summary.getLoosingTrades());
    }
    
    private synchronized void addProfitableTrades(int trades) {
        this.profitableTrades += trades;
    }
    
    private synchronized void addLoosingTrades(int trades) {
        this.loosingTrades += trades;
    }
    
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
