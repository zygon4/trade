/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 * For now this is opaque without any sense of duration/aggregation
 *
 * @author zygon
 */
public class Volatility extends NumericIndication {

    public static Identifier ID = new ID("volatility", Classification.PRICE);
    
    public Volatility(String tradableIdentifier, long timestamp, double value) {
        super(ID, tradableIdentifier, timestamp, value);
    }
}
