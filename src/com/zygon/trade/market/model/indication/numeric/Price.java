/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Classification;

/**
 * This represents the current price.
 *
 * @author zygon
 */
public class Price extends NumericIndication {

    public Price(String tradableIdentifier, Classification classification, long timestamp, double value, Aggregation aggregation) {
        super(NumericIndication.IDS.PRICE, tradableIdentifier, classification, timestamp, value, aggregation);
    }

    public Price(String tradableIdentifier, long timestamp, double value) {
        this(tradableIdentifier, Classification.PRICE, timestamp, value, null);
    }
}
