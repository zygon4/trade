/*
 *
 */
package com.zygon.trade.market.util;

import com.zygon.analysis.util.Duration;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class Aggregation {
    
    private final Type type;
    private final Duration duration;
    private final TimeUnit units;

    public Aggregation(Type type, Duration duration, TimeUnit units) {
        this.type = type;
        this.duration = duration;
        this.units = units;
    }
    
    public Duration getDuration() {
        return this.duration;
    }

    public Type getType() {
        return type;
    }

    public TimeUnit getUnits() {
        return this.units;
    }
    
    public boolean isEqual(Aggregation aggregation) {
        if (aggregation != null) {
            if (aggregation.getDuration() == this.getDuration() && 
                aggregation.getType() == this.getType() && 
                aggregation.getUnits() == this.getUnits()) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public String toString() {
        return String.format("%s_%d_%s", this.getType().name(), this.getDuration(), this.getUnits().name());
    }
}
