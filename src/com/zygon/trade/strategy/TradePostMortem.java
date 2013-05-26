/**
 * 
 */

package com.zygon.trade.strategy;

/**
 *
 * @author zygon
 */
public final class TradePostMortem {

    // TODO: enter/exit conditions
    private final long duration;
    private final double profit;

    public TradePostMortem(long duration, double profit) {
        this.duration = duration;
        this.profit = profit;
    }

    public long getDuration() {
        return this.duration;
    }

    public double getProfit() {
        return this.profit;
    }
    
    public boolean profitableTrade() {
        return this.profit > 0.0;
    }
}
