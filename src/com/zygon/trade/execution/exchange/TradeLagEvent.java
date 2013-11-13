
package com.zygon.trade.execution.exchange;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class TradeLagEvent extends ExchangeEvent {

    private final long value;
    private final TimeUnit units;
    
    public TradeLagEvent(long value, TimeUnit units) {
        super(EventType.TRADE_LAG);
        
        this.value = value;
        this.units = units;
    }
    
    public TimeUnit getUnits() {
        return this.units;
    }

    public long getValue() {
        return this.value;
    }
}
