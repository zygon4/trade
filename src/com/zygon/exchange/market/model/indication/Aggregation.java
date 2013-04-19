/*
 *
 */
package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.market.model.indication.technical.TimeUnits;

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
    
    public Aggregation(Type type, long duration, TimeUnits units) {
        this(type, duration, units.getDesc());
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
        return String.format("[%s][%d %s]", this.getType().name(), this.getDuration(), this.getUnits());
    }
}
