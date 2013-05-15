/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Identifier;

/**
 *
 * @author zygon
 */
public class SimpleMarketIndication extends MarketIndication {

    private final double value;

    public SimpleMarketIndication(Identifier id, String tradableIdentifier, long timestamp, double value) {
        super(id, tradableIdentifier, timestamp);
        
        this.value = value;
    }
    
    public double getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [%s] %d", this.getIdentifier().getID(), this.getValue());
    }
}
