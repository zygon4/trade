/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class NumericIndication extends Indication {
    
    public static class IDS {
        public static Identifier PRICE = new ID("price");
        public static Identifier SMA = new ID("simple moving average");
        public static Identifier VOLUME = new ID("volume");
    }
    
    // TBD: How to handle other things such as: TransactionCurrency??  Use a map? subclass?
    private final double value;
    private final Aggregation aggregation;
    
    public NumericIndication(Identifier id, String tradableIdentifier, Classification classification, long timestamp, 
            double value, Aggregation aggregation) {
        super(id, tradableIdentifier, classification, timestamp);
        this.value = value;
        this.aggregation = aggregation;
    }
    
    public NumericIndication(Identifier id, String tradableIdentifier, Classification classification, long timestamp, double value) {
        this (id, tradableIdentifier, classification, timestamp, value, null);
    }
    
    public Aggregation getAggregation() {
        return this.aggregation;
    }
    
    public double getValue() {
        return this.value;
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
        sb.append(' ');
        sb.append(new Date(this.getTimestamp()));
        
        return sb.toString();
    }
}
