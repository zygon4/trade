/**
 * 
 */

package com.zygon.exchange.market.model.indication.logic.message;

import com.zygon.exchange.market.model.indication.logic.MarketIndication;

/**
 *
 * @author zygon
 */
public class SimpleMarketIndication extends MarketIndication {

    private final double value;

    public SimpleMarketIndication(String tradableIdentifier, String id, long timestamp, Type type, double value) {
        super(tradableIdentifier, id, timestamp, type);
        
        this.value = value;
    }
    
    public double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [%s] %d", this.getType().name(), this.getValue());
    }
}
