/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public class MarketIndication extends Indication {

    // probably not the final resting place..
    public static class IDS {
        public static Identifier BOLLINGER_BAND = new ID("bollinger");
        public static Identifier MACD = new ID("macd");
        public static Identifier SUPPORT = new ID("support");
        public static Identifier RESISTANCE = new ID("resistance");
        public static Identifier RSI = new ID("rsi");
        public static Identifier WVAP = new ID("vwap");
    }
    
    private final Identifier id;
    
    public MarketIndication(Identifier id, String tradableIdentifier, Classification classification, long timestamp) {
        super(id, tradableIdentifier, classification, timestamp);
        
        this.id = id;
    }

    public final Identifier getIdentifier() {
        return this.id;
    }
}
