/*
 *
 */
package com.zygon.trade.market.model.indication;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class Aggregation {
    
    // better name??
    public static enum Type {
        HIGH("max"),
        LOW("min"),
        AVG("avg");
        
        private Type(String val) {
            this.val = val;
        }

        public String getVal() {
            return this.val;
        }

        private final String val;
    }
    
    public static enum Duration {
        _1 (1),
        _4 (4),
        _5 (5),
        _15 (15),
        _30 (30),
        _60 (60),
        _24 (24);
        
        private int val;

        private Duration(int val) {
            this.val = val;
        }

        public int getVal() {
            return this.val;
        }
    }
    
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
