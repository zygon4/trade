/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public class MarketIndication extends Indication {
    
    private final Identifier id;
    
    public MarketIndication(Identifier id, String tradableIdentifier, long timestamp) {
        super(id, tradableIdentifier, timestamp);
        
        this.id = id;
    }

    public final Identifier getIdentifier() {
        return this.id;
    }
}
