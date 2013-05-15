/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class SMA30Min extends SimpleMovingAverage {

    public static Identifier SMA_30_MIN = new ID("simple moving average", Classification.PRICE, new Aggregation(Aggregation.Type.AVG, 30, TimeUnit.MINUTES));
    
    public SMA30Min(String tradableIdentifier, long timestamp, double value) {
        super(SMA_30_MIN, tradableIdentifier, timestamp, value);
    }

    
}
