/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.market.model.indication.Indication;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class Numeric extends Indication {
    
    // TBD: How to handle other things such as: TransactionCurrency??  Use a map?
    private final double value;
    private final Aggregation aggregation;
    
    public Numeric(String tradableIdentifier, String id, long timestamp, double value, Aggregation aggregation) {
        super(tradableIdentifier, id, timestamp);
        this.value = value;
        this.aggregation = aggregation;
    }
    
    public Numeric(String tradableIdentifier, String id, long timestamp, double value) {
        this (tradableIdentifier, id, timestamp, value, null);
    }
    
    public Aggregation getAggregation() {
        return this.aggregation;
    }
    
    public double getValue() {
        return this.value;
    }

    @Override
    public void getStatement(StringBuilder sb, String label) {
        super.getStatement(sb, null);
        
        if (this.aggregation != null) {
            sb.append(".win:ext_timed(timestamp, ")
              .append(this.getAggregation().getDuration()).append(' ')
              .append(this.getAggregation().getUnits()).append(')');
        }
        
        if (label != null) {
            sb.append(" as ").append(label);
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        if (this.getAggregation() != null) {
            sb.append(this.getAggregation()).append(' ');
        }
        
        sb.append(this.getId());
        sb.append('[').append(this.getTradableIdentifier()).append(']').append(", ");
        sb.append(this.getValue());
        sb.append(new Date(this.getTimestamp()));
        
        return sb.toString();
    }
}
