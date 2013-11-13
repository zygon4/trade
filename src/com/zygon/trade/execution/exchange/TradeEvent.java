
package com.zygon.trade.execution.exchange;

/**
 *
 * @author david.charubini
 */
public class TradeEvent extends ExchangeEvent {

    private final long tradeID;
    
    public TradeEvent(long tradeID) {
        super(EventType.TRADE_FILL);
        
        this.tradeID = tradeID;
    }

    public long getTradeID() {
        return this.tradeID;
    }
}
