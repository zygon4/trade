/**
 * 
 */

package com.zygon.exchange.market.model.indication.logic;

import com.zygon.exchange.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public class MarketIndication extends Indication {

    public static enum Type {
        BOLLINGER_BAND("bollinger"),
        MACD ("macd"),
        SUPPORT ("support"),
        RESISTANCE ("resistance"),
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
    
    public MarketIndication(String tradableIdentifier, String id, long timestamp, 
            Type type) {
        super(tradableIdentifier, id, timestamp);
        
        this.type = type;
    }

    public final Type getType() {
        return this.type;
    }
}
