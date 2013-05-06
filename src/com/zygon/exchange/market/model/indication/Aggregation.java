/*
 *
 */
package com.zygon.exchange.market.model.indication;

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
    
    private final Type type;
    private final long duration;
    private final String units;

    public Aggregation(Type type, long duration, String units) {
        this.type = type;
        this.duration = duration;
        this.units = units;
    }
    
    public Aggregation(Type type, long duration, TimeUnit units) {
        this(type, duration, units.name());
    }
    
    public long getDuration() {
        return this.duration;
    }

    public Type getType() {
        return type;
    }

    public String getUnits() {
        return this.units;
    }

    @Override
    public String toString() {
        return String.format("%s_%d_%s", this.getType().name(), this.getDuration(), this.getUnits());
    }
}
