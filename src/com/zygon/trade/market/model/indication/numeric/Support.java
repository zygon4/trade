/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.numeric.NumericIndication;

/**
 *
 * @author zygon
 */
public class Support extends NumericIndication {

    public static Identifier SUPPORT = new ID("support", Classification.PRICE);
    
    public Support(String tradableIdentifier, long timestamp, double value) {
        super(SUPPORT, tradableIdentifier, timestamp, value);
    }
}
