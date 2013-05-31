/**
 * 
 */

package com.zygon.trade.execution;

import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.model.indication.numeric.Price;
import com.zygon.trade.market.util.IndicationStore;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 
 * @author zygon
 */
public final class MarketConditions {
    
    private final Map<Identifier, Indication> indicationsByIdentifier = new HashMap<>();
    private final IndicationStore indicationStore = new IndicationStore();
    
    private final String marketIdentifier;
    // would rather not have to worry about the tradeableidentifier at this leve
    // but rather just store/retrieve whatever the users want.
    private final String tradeableIdentifier;
    
    public MarketConditions(String marketIdentifier, String tradeableIdentifier) {
        this.marketIdentifier = marketIdentifier;
        this.tradeableIdentifier = tradeableIdentifier;
    }

    public Price getPrice() {
        return (Price)this.getIndication(Price.ID);
    }
    
    public synchronized Indication getIndication(Identifier id) {
        return this.indicationsByIdentifier.get(id);
    }

    public String getMarketIdentifier() {
        return this.marketIdentifier;
    }

    public String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }
    
    public synchronized void putIndication(Indication indication) {
        if (!indication.getTradableIdentifier().equals(this.tradeableIdentifier)) {
            throw new IllegalArgumentException("Invalid");
        }
        
        this.indicationsByIdentifier.put(indication.getId(), indication);
    }
}
