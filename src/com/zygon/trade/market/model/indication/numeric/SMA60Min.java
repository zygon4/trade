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
public class SMA60Min extends SimpleMovingAverage {

    public static Identifier SMA_60_MIN = new ID("simple moving average", Classification.PRICE, new Aggregation(Aggregation.Type.AVG, 60, TimeUnit.MINUTES));
    
    public SMA60Min(String tradableIdentifier, long timestamp, double value) {
        super(SMA_60_MIN, tradableIdentifier, timestamp, value);
    }
}
