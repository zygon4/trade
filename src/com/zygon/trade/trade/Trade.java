
package com.zygon.trade.trade;

/**
 *
 * @author zygon
 */
public class Trade {
    
    private final TradeSignal[] tradeSignals;

    private long id = -1;
    
    // TODO: Order sensitivity: eg parallel execution vs in-order
    
    public Trade(TradeSignal ...tradeSignals) {
        if (tradeSignals == null || tradeSignals.length == 0) {
            throw new IllegalStateException("Trade signals must be provided.");
        }
        this.tradeSignals = tradeSignals;
    }

    public long getId() {
        return this.id;
    }
    
    public TradeSignal[] getTradeSignals() {
        return this.tradeSignals;
    }

    /*pkg*/ final void setId(long id) {
        this.id = id;
    }
}
