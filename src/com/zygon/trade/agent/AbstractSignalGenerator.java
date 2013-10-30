
package com.zygon.trade.agent;

import com.zygon.trade.execution.MarketConditions;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Indication;

/**
 * @author zygon
 */
public class AbstractSignalGenerator implements SignalGenerator {

    
    private final SignalGenerator signalGenerator;
    private final MarketConditions marketConditions = new MarketConditions("MtGox");
    
    public AbstractSignalGenerator(SignalGenerator signalGenerator) {
        this.signalGenerator = signalGenerator;
    }
    
    protected final MarketConditions getMarketConditions() {
        return this.marketConditions;
    }
    
    @Override
    public TradeSignal getSignal(Message message) {
        
        Indication indication = (Indication) message;
        this.marketConditions.putIndication(indication, null);
        
        return this.signalGenerator.getSignal(message);
    }
}
