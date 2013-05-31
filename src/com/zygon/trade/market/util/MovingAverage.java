/**
 * 
 */

package com.zygon.trade.market.util;

import org.apache.commons.math3.stat.descriptive.SynchronizedDescriptiveStatistics;

/**
 * 
 * Not really a moving average so much as a collection of stats..
 *
 * @author zygon
 * 
 */
public class MovingAverage {
    
    private final SynchronizedDescriptiveStatistics values; 

    public MovingAverage(int maxValues) {
        this.values = new SynchronizedDescriptiveStatistics(maxValues);
    }
    
    public void add (double value) {
        this.values.addValue(value);
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
    
    public double getStd() {
        return this.values.getStandardDeviation();
    }
}
