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
 * 
 * From Investopedia:
 * 
 * "1. Crossovers - As shown in the chart above, when the MACD falls below the 
 * signal line, it is a bearish signal, which indicates that it may be time to 
 * sell. Conversely, when the MACD rises above the signal line, the indicator 
 * gives a bullish signal, which suggests that the price of the asset is likely 
 * to experience upward momentum. Many traders wait for a confirmed cross above 
 * the signal line before entering into a position to avoid getting getting 
 * "faked out" or entering into a position too early, as shown by the first 
 * arrow. 
 * 
 * 2. Divergence - When the security price diverges from the MACD. It signals 
 * the end of the current trend. 
 * 
 * 3. Dramatic rise - When the MACD rises dramatically - that is, the shorter 
 * moving average pulls away from the longer-term moving average - it is a 
 * signal that the security is overbought and will soon return to normal levels.
 * 
 * Traders also watch for a move above or below the zero line because this 
 * signals the position of the short-term average relative to the long-term 
 * average. When the MACD is above zero, the short-term average is above the 
 * long-term average, which signals upward momentum. The opposite is true when 
 * the MACD is below zero. As you can see from the chart above, the zero line 
 * often acts as an area of support and resistance for the indicator."
 */
public class MACD extends MarketIndication {

    public static Identifier ID = new ID("macd", Classification.PRICE);
    
    public static enum IndicationType {
        CROSSOVER,
        DIVERGENCE,
        ZERO_CROSS;
    }

    private final IndicationType type;
    
    public MACD(String tradableIdentifier, long timestamp, IndicationType type) {
        super(ID, tradableIdentifier, timestamp);
        
        this.type = type;
    }

    public IndicationType getIndicationType() {
        return this.type;
    }
}
