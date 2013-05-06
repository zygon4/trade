/**
 * 
 */

package com.zygon.exchange.market.model.indication.numeric;

import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class MovingAverage extends NumericIndication {

    public MovingAverage(String tradableIdentifier, long timestamp, double value, Aggregation aggregation) {
        super (tradableIdentifier, Classification.PRICE, timestamp, value, aggregation);
        
        if (aggregation.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregation type must be Average");
        }
    }
}
