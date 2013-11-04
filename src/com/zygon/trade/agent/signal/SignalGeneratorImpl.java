
package com.zygon.trade.agent.signal;

import com.zygon.trade.agent.SignalGenerator;
import com.zygon.trade.agent.TradeSignal;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Indication;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zygon
 */
public abstract class SignalGeneratorImpl implements SignalGenerator {
    
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
    
    protected abstract Collection<TradeSignal> doGetSignal(Indication indication);
    
    @Override
    public Collection<TradeSignal> getSignal(Message message) {
        
        Indication indication = (Indication) message;
        
        this.marketConditions.putIndication(indication, null);
        
        Collection<Indication> additionalIndications = new ArrayList<Indication>();
        
        this.getAdditionalIndications(additionalIndications);
        
        if (!additionalIndications.isEmpty()) {
            for (Indication additional : additionalIndications) {
                this.marketConditions.putIndication(additional, null);
            }
        }
        
        return this.doGetSignal(indication);
    }
}
