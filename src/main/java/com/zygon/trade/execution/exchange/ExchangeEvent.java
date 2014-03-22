package com.zygon.trade.execution.exchange;

/**
 *
 * @author davec
 */
public class ExchangeEvent {
    
    public static enum EventType {
        ACCOUNT_STATUS,
        CONNECTED,
        DISCONNECTED,
        ERROR,
        TICKER,
        TRADE_CANCEL,
        TRADE_FILL,
        TRADE_LAG,
        TRADE_REJECTED,
        // What else?
    }
    
    private final EventType eventType;

    public ExchangeEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public String getDisplayString() {
        return this.eventType.name();
    }
    
    public EventType getEventType() {
        return this.eventType;
    }
}

