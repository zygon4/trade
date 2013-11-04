
package com.zygon.trade.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the door to the trading floor. All trades come through here.
 * A trade will be accepted or rejected (tbd) and processed accordingly.
 *
 * @author zygon
 */
public class TradeGateway {

    private final Logger log;

    public TradeGateway() {
        this.log = LoggerFactory.getLogger(TradeGateway.class);
    }
    
    public void process (TradeSignal signal) {
        this.log.trace("Processing signal: " + signal);
    }
}
