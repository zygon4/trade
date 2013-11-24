/**
 * 
 */

package com.zygon.trade.market.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 * 
 */
public class MovingAverage {
    
    private final TimeBasedDescriptiveStatistics values; 

    private static TimeBasedDescriptiveStatistics create(Duration duration, TimeUnit timeUnits) {
        return new TimeBasedDescriptiveStatistics(duration, timeUnits);
    }
    
    public MovingAverage (Duration duration, TimeUnit timeUnits) {
        this.values = create(duration, timeUnits);
    }
    
    public void add (double value, Date timestamp) {
        this.values.addValue(value, timestamp);
    }
    
    public double getMean() {
        return this.values.getMean();
    }
    
    public double getHigh() {
        return this.values.getMax();
    }
    
    public double getLow() {
        return this.values.getMin();
    }
    
    public long getAvailableValues() {
        return this.values.getN();
    }
    
    public double getStd() {
        return this.values.getStandardDeviation();
    }
    
    public double[] getValues() {
        return this.values.getValues();
    }
}
