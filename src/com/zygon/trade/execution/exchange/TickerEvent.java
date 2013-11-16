
package com.zygon.trade.execution.exchange;

import com.zygon.trade.market.data.Ticker;

/**
 *
 * @author zygon
 */
public class TickerEvent extends ExchangeEvent {

    private final Ticker ticker;
    
    public TickerEvent(Ticker ticker) {
        super(EventType.TICKER);
        
        this.ticker = ticker;
    }

    public Ticker getTicker() {
        return this.ticker;
    }
}
