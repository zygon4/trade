/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 *
 * @author zygon
 */
public class Support extends SimpleMarketIndication {

    public static Identifier SUPPORT = new ID("support");
    
    public Support(String tradableIdentifier, long timestamp, double value) {
        super(SUPPORT, tradableIdentifier, timestamp, value);
    }
}
