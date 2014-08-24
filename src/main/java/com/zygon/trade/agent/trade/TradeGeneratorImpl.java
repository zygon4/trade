
package com.zygon.trade.agent.trade;

import com.google.common.collect.Lists;
import com.zygon.trade.trade.TradeGenerator;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.market.model.indication.Indication;
import java.util.Collection;

/**
 * @author zygon
 */
public abstract class TradeGeneratorImpl implements TradeGenerator {
    
    private final MarketConditions marketConditions = new MarketConditions("MtGox");
    
    protected final MarketConditions getMarketConditions() {
        return this.marketConditions;
    }
    
    /**
     * Returns additional indications which will be provided to the market conditions.     
     * @param additional
     */
    protected void getAdditionalIndications(Collection<Indication> additional) {
        // do nothing
    }
    
    @Override
    public void notify(Indication message) {
        Indication indication = (Indication) message;
        
        this.marketConditions.putIndication(indication, null);
        
        Collection<Indication> additionalIndications = Lists.newArrayList();
        
        this.getAdditionalIndications(additionalIndications);
        
        if (!additionalIndications.isEmpty()) {
            for (Indication additional : additionalIndications) {
                this.marketConditions.putIndication(additional, null);
            }
        }
    }
}
