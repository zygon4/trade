/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class SimpleMovingAverage extends NumericIndication {

    public SimpleMovingAverage(String tradableIdentifier, Classification classification, long timestamp, double value, Aggregation aggregation) {
        super (NumericIndication.IDS.SMA, tradableIdentifier, classification, timestamp, value, aggregation);
        
        if (aggregation.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregation type must be Average");
        }
    }
}
