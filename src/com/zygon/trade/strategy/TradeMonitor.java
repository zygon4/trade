/**
 * 
 */

package com.zygon.trade.strategy;

/**
 * Skeleton right now.. to be used to keep a watchful eye on how the active
 * trades are.
 *
 * @author zygon
 */
public class TradeMonitor {
    
    // TBD: "valid" flag to state that the monitor is active on a trade?

    private volatile double profit = 0.0;
    private volatile double duration = 0.0;

    public final double getProfit() {
        return this.profit;
    }

    public final double getDuration() {
        return this.duration;
    }
    
    /*pkg*/ final void setDuration(long duration) {
        this.duration = duration;
    }

    /*pkg*/ final void setProfit(double profit) {
        this.profit = profit;
    }
}
