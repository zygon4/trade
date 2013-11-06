
package com.zygon.trade.agent.signal;

import com.zygon.trade.trade.TradeGenerator;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.trade.Trade;
import java.util.ArrayList;
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
    
    protected abstract Collection<Trade> getTrades();
    
    @Override
    public Collection<Trade> getTrades(Message message) {
        
        Indication indication = (Indication) message;
        
        this.marketConditions.putIndication(indication, null);
        
        Collection<Indication> additionalIndications = new ArrayList<Indication>();
        
        this.getAdditionalIndications(additionalIndications);
        
        if (!additionalIndications.isEmpty()) {
            for (Indication additional : additionalIndications) {
                this.marketConditions.putIndication(additional, null);
            }
        }
        
        return this.getTrades();
    }
}
