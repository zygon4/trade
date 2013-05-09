/**
 * 
 */

package com.zygon.exchange.market.model.indication.market;

import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class SimpleMarketIndication extends MarketIndication {

    private final double value;

    public SimpleMarketIndication(String tradableIdentifier, Classification classification, 
            long timestamp, Type type, double value) {
        super(tradableIdentifier, classification, timestamp, type);
        
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
