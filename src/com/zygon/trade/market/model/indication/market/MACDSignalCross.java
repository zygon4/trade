/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 *
 * @author zygon
 */
public class MACDSignalCross extends MACD {

    public static Identifier ID = new ID("macd-signal-cross", Classification.PRICE);
    
    private final boolean crossAboveSignal;
    
    public MACDSignalCross(String tradableIdentifier, long timestamp, boolean crossAboveSignal) {
        super(ID, tradableIdentifier, timestamp, IndicationType.SIGNAL_CROSS);
        
        this.crossAboveSignal = crossAboveSignal;
    }

    public boolean crossAboveSignal() {
        return this.crossAboveSignal;
    }

    @Override
    public String toString() {
        return super.toString() + " cross " + (this.crossAboveSignal ? "above" : "below") + " signal";
    }
}
