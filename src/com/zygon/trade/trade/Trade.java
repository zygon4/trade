
package com.zygon.trade.trade;

/**
 *
 * @author zygon
 */
public class Trade {
    
    private final String name;
    private final TradeSignal[] tradeSignals;

    private long id = -1;
    private long lastStartTime = -1;
    private long lastDuration = -1;
    
    // TODO: Order sensitivity: eg parallel execution vs in-order
    
    public Trade(String name, TradeSignal ...tradeSignals) {
        this.name = name;
        this.tradeSignals = tradeSignals;
    }

    public long getId() {
        return this.id;
    }

    public long getLastDuration() {
        return this.lastDuration;
    }

    public String getName() {
        return this.name;
    }
    
    public TradeSignal[] getTradeSignals() {
        return this.tradeSignals;
    }

    /*pkg*/ final void setId(long id) {
        this.id = id;
    }
    
    /*pkg*/ void notifyClosed(long ts) {
        if (ts < this.lastStartTime) {
            throw new IllegalStateException("End time cannot be less than start time");
        }
        
        this.lastDuration = ts - this.lastStartTime;
        this.lastStartTime = -1;
    }
    
    // Notifies the Trade's particular signal that it's open
    /*pkg*/ void notifyOpen(long ts) {
        if (this.lastStartTime != -1) {
            throw new IllegalStateException("");
        }
        this.lastStartTime = ts;
        this.lastDuration = -1;
    }
}
