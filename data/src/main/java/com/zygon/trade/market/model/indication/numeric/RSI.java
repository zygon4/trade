/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.util.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 * Basis RSI without a sense of duration
 *
 * @author zygon
 */
public class RSI extends NumericIndication {

    public static Identifier ID = new ID("RSI", Classification.PRICE);
    
    public RSI(String tradableIdentifier, long timestamp, double value) {
        super(ID, tradableIdentifier, timestamp, value);
    }
}
