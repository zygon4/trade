
package com.zygon.trade.execution.exchange;

/**
 *
 * @author zygon
 */
public class ExchangeError extends ExchangeEvent {

    private final String message;
    
    public ExchangeError(String message) {
        super(EventType.ERROR);
        
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
