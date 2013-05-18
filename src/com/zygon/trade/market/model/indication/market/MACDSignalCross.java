/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

/**
 *
 * @author zygon
 */
public class MACDSignalCross extends MACD {

    private final boolean crossAboveSignal;
    
    public MACDSignalCross(String tradableIdentifier, long timestamp, boolean crossAboveSignal) {
        super(tradableIdentifier, timestamp, IndicationType.SIGNAL_CROSS);
        
        this.crossAboveSignal = crossAboveSignal;
    }

    public boolean crossAboveSignal() {
        return this.crossAboveSignal;
    }
}
