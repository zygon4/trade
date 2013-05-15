/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Aggregation.Duration;
import static com.zygon.trade.market.model.indication.Aggregation.Duration._15;
import static com.zygon.trade.market.model.indication.Aggregation.Duration._30;
import static com.zygon.trade.market.model.indication.Aggregation.Duration._60;
import com.zygon.trade.market.model.indication.Identifier;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 *
 * @author zygon
 */
public class SimpleMovingAverage extends NumericIndication {

    public SimpleMovingAverage(Identifier id, String tradableIdentifier, long timestamp, double value) {
        super (id, tradableIdentifier, timestamp, value);
    }
    
    // So this is a ghetto way of locating an id at runtime.. it's fairly 
    // rediculous and scales poorly.
    private static Identifier find(Duration duration, TimeUnit units) {
        Identifier id = null;
        
        switch (units) {
            case MINUTES:
                switch (duration) {
                    case _15:
                        id = SMA15Min.ID;
                        break;
                    case _30:
                        id = SMA30Min.ID;
                        break;
                    case _60:
                        id = SMA60Min.ID;
                        break;
                }
                break;
        }
        
        return id;
    }
    
    public SimpleMovingAverage(String tradableIdentifier, Duration duration, TimeUnit units, long timestamp, double value) {
        this(find(duration, units), tradableIdentifier, timestamp, value);
    }
}
