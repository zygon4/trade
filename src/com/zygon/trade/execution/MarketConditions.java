/**
 * 
 */

package com.zygon.trade.execution;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.model.indication.numeric.Price;
import com.zygon.trade.market.util.IndicationStore;

/**
 *
 * 
 * @author zygon
 */
public final class MarketConditions {
    
    private final IndicationStore indicationStore = new IndicationStore();
    private final String marketIdentifier;
    
    public MarketConditions(String marketIdentifier) {
        this.marketIdentifier = marketIdentifier;
    }

    public Price getPrice(String tradeableIdentifier) {
        return (Price)this.getIndication(Price.ID, tradeableIdentifier);
    }
    
    public synchronized Indication getIndication(Identifier id, String tradeableIdentifier, Aggregation aggregation) {
        return this.indicationStore.get(id, tradeableIdentifier, aggregation);
    }

    public synchronized Indication getIndication(Identifier id, String tradeableIdentifier) {
        return this.getIndication(id, tradeableIdentifier, null);
    }
    
    public String getMarketIdentifier() {
        return this.marketIdentifier;
    }
    
    public synchronized void putIndication(Indication indication, Aggregation aggregation) {
        this.indicationStore.put(indication, aggregation);
    }
}
