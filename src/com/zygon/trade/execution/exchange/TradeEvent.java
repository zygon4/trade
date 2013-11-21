
package com.zygon.trade.execution.exchange;

/**
 *
 * @author david.charubini
 */
public class TradeEvent extends ExchangeEvent {

    private final String tradeID;
    
    public TradeEvent(String tradeID) {
        super(EventType.TRADE_FILL);
        
        this.tradeID = tradeID;
    }

    @Override
    public String getDisplayString() {
        String display = super.getDisplayString();
        
        display += (":"+this.tradeID);
        
        return display;
    }

    public String getOrderID() {
        return this.tradeID;
    }
}
