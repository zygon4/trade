/**
 * 
 */

package com.zygon.trade.strategy;

/**
 *
 * @author zygon
 */
public final class TradePostMortem {

    private final Signal entry;
    private final Signal exit;
    private final long duration;
    private final double profit;

    public TradePostMortem(Signal entry, Signal exit, long duration, double profit) {
        this.entry = entry;
        this.exit = exit;
        this.duration = duration;
        this.profit = profit;
    }

    public long getDuration() {
        return this.duration;
    }

    public Signal getEntrySignal() {
        return this.entry;
    }

    public Signal getExitSignal() {
        return this.exit;
    }
    
    public double getProfit() {
        return this.profit;
    }
    
    public boolean profitableTrade() {
        return this.profit > 0.0;
    }
}
