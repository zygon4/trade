/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.IndicationListener;

/**
 *
 * @author zygon
 */
public class NumericIndicationListener extends IndicationListener<NumericIndication> {
    
    private static String getName(Classification clazz, Aggregation aggregation) {
        return aggregation+"_"+clazz.name();
    }
    
    private final Aggregation aggregation;

    public NumericIndicationListener(Aggregation aggregation, Classification classification) {
        super(getName(classification, aggregation));
        
        this.aggregation = aggregation;
    }
    
    public Aggregation getAggregation() {
        return aggregation;
    }
}
