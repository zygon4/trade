/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public class MarketIndication extends Indication {

    public static enum Type {
        BOLLINGER_BAND ("bollinger"),
        MACD ("macd"),
        SUPPORT ("support"),
        RESISTANCE ("resistance"),
        RSI ("rsi"),
        WVAP ("vwap");
    
        private final String id;

        private Type(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }
    
    private final Type type;
    
    public MarketIndication(String tradableIdentifier, Classification classification, long timestamp, 
            Type type) {
        super(tradableIdentifier, classification, timestamp);
        
        this.type = type;
    }

    public final Type getType() {
        return this.type;
    }
}
