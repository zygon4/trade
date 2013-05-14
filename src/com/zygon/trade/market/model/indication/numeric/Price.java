/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;

/**
 * This represents the current price.
 *
 * @author zygon
 */
public class Price extends NumericIndication {

    public Price(String tradableIdentifier, long timestamp, double value) {
        super(NumericIndication.IDS.PRICE, tradableIdentifier, Classification.PRICE, timestamp, value);
    }
}
