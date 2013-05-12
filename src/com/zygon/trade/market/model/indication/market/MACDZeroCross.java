/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

/**
 *
 * @author zygon
 */
public class MACDZeroCross extends MACD {

    private final boolean crossAboveZero;
    
    public MACDZeroCross(String tradableIdentifier, long timestamp, boolean crossAboveZero) {
        super(tradableIdentifier, timestamp, IndicationType.ZERO_CROSS);
        
        this.crossAboveZero = crossAboveZero;
    }

    public boolean crossAboveZero() {
        return this.crossAboveZero;
    }
}
