
package com.zygon.trade.execution.exchange;

/**
 *
 * @author zygon
 */
public class TradeCancelEvent extends ExchangeEvent {

    private final String orderId;
    private final String reason;
    
    public TradeCancelEvent(String orderId, String reason) {
        super(EventType.TRADE_CANCEL);
        
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getReason() {
        return this.reason;
    }
}
