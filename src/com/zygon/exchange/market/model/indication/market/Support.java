/**
 * 
 */

package com.zygon.exchange.market.model.indication.market;

import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class Support extends SimpleMarketIndication {

    public Support(String tradableIdentifier, long timestamp, double value) {
        super(tradableIdentifier, Classification.PRICE, timestamp, Type.SUPPORT, value);
    }
}
