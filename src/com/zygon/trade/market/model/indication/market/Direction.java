
package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public class Direction extends Indication {

    public static enum MarketDirection {
        UP, 
        DOWN, 
        NEUTRAL;
    }
    
    public static Identifier ID = new ID("direction", Classification.PRICE);
    
    private final MarketDirection marketDirection;
    
    public Direction(String tradableIdentifier, long timestamp, MarketDirection marketDirection) {
        super(ID, tradableIdentifier, timestamp);
        
        this.marketDirection = marketDirection;
    }

    public MarketDirection getMarketDirection() {
        return this.marketDirection;
    }
}
