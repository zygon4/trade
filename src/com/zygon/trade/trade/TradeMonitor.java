/**
 * 
 */

package com.zygon.trade.trade;

/**
 * Skeleton right now.. to be used to keep a watchful eye on how the active
 * trades are.
 *
 * @author zygon
 */
public class TradeMonitor {
    
    // TBD: "valid" flag to state that the monitor is active on a trade?

    private long start = 0;
    private long end = 0;
    private TradeSignal signal;
    private double enterPrice;
    private String tradeId;
    
    private volatile double profit = 0.0;
    
    public final double getProfit() {
        return this.profit;
    }

    public final long getDuration() {
        return this.end - this.start;
    }

    public double getEnterPrice() {
        return this.enterPrice;
    }

    public TradeSignal getSignal() {
        return this.signal;
    }

    public String getTradeId() {
        return this.tradeId;
    }
    
    public void setEnd(long end) {
        this.end = end;
    }

    public void setEnterPrice(double enterPrice) {
        this.enterPrice = enterPrice;
    }

    /*pkg*/ final void setProfit(double profit) {
        this.profit = profit;
    }

    public void setSignal(TradeSignal signal) {
        this.signal = signal;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
}
