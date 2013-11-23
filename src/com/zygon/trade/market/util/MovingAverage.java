/**
 * 
 */

package com.zygon.trade.market.util;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 *
 * @author zygon
 * 
 */
public class MovingAverage {
    
    /**
     * Returns the Date which represents the earliest allowable time in the
     * moving average calculation.
     * 
     * @param duration
     * @param timeUnit
     * @return 
     */
    private static Date calculateStartDate(Duration duration, TimeUnit timeUnit) {
        
        Calendar cal = Calendar.getInstance();
        
        switch (timeUnit) {
            case DAYS:
                cal.add(Calendar.DATE, -duration.getVal());
                break;
            case HOURS:
                cal.add(Calendar.HOUR, -duration.getVal());
                break;
            case MILLISECONDS:
                cal.add(Calendar.MILLISECOND, -duration.getVal());
                break;
            case MINUTES:
                cal.add(Calendar.MINUTE, -duration.getVal());
                break;
            case SECONDS:
                cal.add(Calendar.SECOND, -duration.getVal());
                break;
            default:
                throw new UnsupportedOperationException(timeUnit.name());
        }
        
        return new Date(cal.getTimeInMillis());
    }
    
    private final TimeBasedDescriptiveStatistics values; 

    public MovingAverage(Date start) {
        this.values = new TimeBasedDescriptiveStatistics(start);
    }
    
    public MovingAverage (Duration duration, TimeUnit timeUnits) {
        this(calculateStartDate(duration, timeUnits));
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
