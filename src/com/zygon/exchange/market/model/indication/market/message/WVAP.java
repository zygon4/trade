/**
 * 
 */

package com.zygon.exchange.market.model.indication.market.message;

import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class WVAP extends SimpleMarketIndication {

    public WVAP(String tradableIdentifier, long timestamp, double vwap) {
        super(tradableIdentifier, Classification.PRICE, timestamp, Type.WVAP, vwap);
    }
}
