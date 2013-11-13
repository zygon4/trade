package com.zygon.trade.execution.exchange;

/**
 *
 * @author davec
 */
public class ExchangeEvent {
    
    public static enum EventType {
        ACCOUNT_STATUS,
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

    public EventType getEventType() {
        return this.eventType;
    }
}

