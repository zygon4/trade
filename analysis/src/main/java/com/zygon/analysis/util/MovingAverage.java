/**
 * 
 */

package com.zygon.analysis.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 *
 * @author zygon
 * 
 */
public class MovingAverage {
    
    public static interface ValueFn {
        public double getValue (double value, Date date);
    }
    
    public static class ExponentialValueFn implements ValueFn {

        private final double alpha;
        private volatile Double lastValue = null;

        public ExponentialValueFn(double alpha) {
            this.alpha = alpha;
        }

        public ExponentialValueFn() {
            this(0.15);
        }
    
        @Override
        public double getValue(double value, Date date) {
            double newValue = 0.0;
        
            if (this.lastValue == null) {
                this.lastValue = value;
                newValue = this.lastValue;
            } else {
                newValue = this.lastValue + (this.alpha * (value - this.lastValue));
                this.lastValue = newValue;
            }

            return newValue;
        }
    }
    
    private static final int HOURS_IN_DAY = 24;
    private static final int MINUTES_IN_HOUR = 60;
    private static final int SECONDS_IN_MINUTE = 60;
    private static final int MILLIS_IN_SECOND = 1000;
    private static final int NANOS_IN_MILLI = 1000;
    
    protected static int getWindow (Duration duration, TimeUnit timeUnits, int itemsPerMinute) {
        int window = 0;
        
        switch (timeUnits) {
            case DAYS:
                window = duration.getVal() * HOURS_IN_DAY * MINUTES_IN_HOUR * itemsPerMinute;
                break;
            case HOURS:
                window = duration.getVal() * MINUTES_IN_HOUR * itemsPerMinute;
                break;
            case MINUTES:
                window = duration.getVal() * itemsPerMinute;
                break;
            case SECONDS:
            case MICROSECONDS:
            case MILLISECONDS:
            case NANOSECONDS:
                throw new IllegalArgumentException("unsupported aggregation time");
        }
        
        return window;
    }
    
    private final TimeBasedDescriptiveStatistics values;
    private final DescriptiveStatistics stats;
    private final ValueFn valueFn;
    
    private static TimeBasedDescriptiveStatistics create(Duration duration, TimeUnit timeUnits) {
        return new TimeBasedDescriptiveStatistics(duration, timeUnits);
    }
    
    public MovingAverage (DescriptiveStatistics values, ValueFn valueFn) {
        this.stats = values;
        
        if (values instanceof TimeBasedDescriptiveStatistics) {
            this.values = (TimeBasedDescriptiveStatistics) values;
        } else {
            this.values = null;
        }
        
        this.valueFn = valueFn;
    }
    
    public MovingAverage (DescriptiveStatistics values) {
        this(values, null);
    }
    
    public MovingAverage (Duration duration, TimeUnit timeUnits, ValueFn valueFn, int itemsPerMinute) {
        this (new DescriptiveStatistics(getWindow(duration, timeUnits, itemsPerMinute)), valueFn);
    }
    
    public MovingAverage (Duration duration, TimeUnit timeUnits, ValueFn valueFn) {
        this (create (duration, timeUnits), valueFn);
    }
    
    public MovingAverage (Duration duration, TimeUnit timeUnits, int itemsPerMinute) {
        this (new DescriptiveStatistics(getWindow(duration, timeUnits, itemsPerMinute)));
    }
    
    public MovingAverage (Duration duration, TimeUnit timeUnits) {
        this (create (duration, timeUnits), null);
    }
    
    public void add (double value, Date timestamp) {
        
        if (this.valueFn != null) {
            value = this.valueFn.getValue(value, timestamp);
        }
        
        if (this.values != null) {
            this.values.addValue(value, timestamp);
        } else {
            this.stats.addValue(value);
        }
    }
    
    public void add (double value) {
        this.add(value, new Date());
    }
    
    public double getMean() {
        return this.stats.getMean();
    }
    
    public double getHigh() {
        return this.stats.getMax();
    }
    
    public double getLow() {
        return this.stats.getMin();
    }
    
    public long getAvailableValues() {
        return this.stats.getN();
    }
    
    public double getStd() {
        return this.stats.getStandardDeviation();
    }
    
    public double[] getValues() {
        return this.stats.getValues();
    }
}
