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
 */
public class WVAP extends SimpleMarketIndication {

    public static Identifier WVAP = new ID("vwap", Classification.PRICE);
    
    public WVAP(String tradableIdentifier, long timestamp, double vwap) {
        super(WVAP, tradableIdentifier, timestamp, vwap);
    }
}
