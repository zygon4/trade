/**
 * 
 */

package com.zygon.exchange.market.model.indication.numeric;

import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.market.model.indication.IndicationListener;

/**
 *
 * @author zygon
 */
public class NumericIndicationListener extends IndicationListener<NumericIndication> {
    
    private static String getName(Classification clazz, Aggregation aggregation) {
        return aggregation+"_"+clazz.name();
    }
    
    private final Aggregation aggregation;

    public NumericIndicationListener(Aggregation aggregation, String tradeableIdentifier, 
            Classification classification) {
        super(getName(classification, aggregation), tradeableIdentifier);
        
        this.aggregation = aggregation;
    }
    
    public NumericIndicationListener (String security, Classification classification, Aggregation aggregation) {
        this(aggregation, security, classification);
    }

    public Aggregation getAggregation() {
        return aggregation;
    }
}
