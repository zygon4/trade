/**
 * 
 */

package com.zygon.trade.market.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class ExponentialMovingAverage extends MovingAverage {

    private final double alpha;
    private volatile Double lastValue = null;
    
    // TODO: calculate alpha from duration        return 2.0 / (maxValues + 1.0);
    
    public ExponentialMovingAverage (Duration duration, TimeUnit timeUnits) {
        super (duration, timeUnits);
        this.alpha = 0.5;
    }
        
    public ExponentialMovingAverage (Date start) {
        super(start);
        this.alpha = 0.5;
    }
    
    @Override
    public void add (double value, Date date) {
        double newValue = 0.0;
        
        if (this.lastValue == null) {
            this.lastValue = value;
            newValue = this.lastValue;
        } else {
            newValue = this.lastValue + (this.alpha * (value - this.lastValue));
            this.lastValue = newValue;
        }
        
        super.add(newValue, date);
    }
}
