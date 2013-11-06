
package com.zygon.trade.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the door to the trading floor. All trades come through here.
 * A trade will be accepted or rejected (tbd) and processed accordingly.
 *
 * @author zygon
 */
public class Gateway {

    private final Logger log;

    public Gateway() {
        this.log = LoggerFactory.getLogger(Gateway.class);
    }
    
    public void process (Trade trade) {
        this.log.trace("Processing trade: " + trade);
    }
}
