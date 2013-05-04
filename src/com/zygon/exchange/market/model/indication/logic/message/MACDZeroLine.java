/**
 * 
 */

package com.zygon.exchange.market.model.indication.logic.message;

/**
 *
 * @author zygon
 */
public class MACDZeroLine extends MACD {

    private final boolean crossAboveZero;
    
    public MACDZeroLine(String tradableIdentifier, String id, long timestamp, boolean crossAbvoveZero) {
        super(tradableIdentifier, id, timestamp, IndicationType.ZERO_LINE);
        
        this.crossAboveZero = crossAbvoveZero;
    }

    public boolean crossAboveZero() {
        return this.crossAboveZero;
    }
}
