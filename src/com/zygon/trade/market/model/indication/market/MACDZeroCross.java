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
public class MACDZeroCross extends MACD {

    public static Identifier ID = new ID("macd-zero-cross", Classification.PRICE);
    
    private final boolean crossAboveZero;
    
    public MACDZeroCross(String tradableIdentifier, long timestamp, boolean crossAboveZero) {
        super(ID, tradableIdentifier, timestamp, IndicationType.ZERO_CROSS);
        
        this.crossAboveZero = crossAboveZero;
    }

    public boolean crossAboveZero() {
        return this.crossAboveZero;
    }
}
