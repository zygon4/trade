
package com.zygon.trade.agent.signal;

import com.zygon.trade.agent.SignalGenerator;
import com.zygon.trade.trade.TradeSignal;
import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Indication;

/**
 * @author zygon
 */
public abstract class SignalGeneratorImpl implements SignalGenerator {
    
    private final MarketConditions marketConditions = new MarketConditions("MtGox");
    
    protected final MarketConditions getMarketConditions() {
        return this.marketConditions;
    }
    
    protected abstract TradeSignal doGetSignal(Indication indication);
    
    @Override
    public TradeSignal getSignal(Message message) {
        
        Indication indication = (Indication) message;
        this.marketConditions.putIndication(indication, null);
        
        return this.doGetSignal(indication);
    }
}
