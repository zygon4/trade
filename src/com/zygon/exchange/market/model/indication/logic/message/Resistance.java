/**
 * 
 */

package com.zygon.exchange.market.model.indication.logic.message;

import com.zygon.exchange.market.model.indication.logic.MarketIndication;

/**
 *
 * @author zygon
 */
public class Resistance extends MarketIndication {

    private final double level;

    public Resistance(String tradableIdentifier, String id, long timestamp, double level) {
        super(tradableIdentifier, id, timestamp, Type.RESISTANCE);
        
        this.level = level;
    }
    
    public double getLevel() {
        return this.level;
    }
}
