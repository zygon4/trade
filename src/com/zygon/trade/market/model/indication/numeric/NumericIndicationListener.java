/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.IndicationListener;
import com.zygon.trade.strategy.IndicationProcessor;

/**
 *
 * @author zygon
 */
public class NumericIndicationListener extends IndicationListener<NumericIndication> {
    
    private static String getName(Classification clazz, Aggregation aggregation) {
        return aggregation+"_"+clazz.name();
    }
    
    private final Aggregation aggregation;
    
    public NumericIndicationListener(Aggregation aggregation, Classification classification, IndicationProcessor<NumericIndication> processor) {
        super(getName(classification, aggregation), classification, processor);
        
        this.aggregation = aggregation;
    }
    
    public Aggregation getAggregation() {
        return this.aggregation;
    }

    @Override
    protected boolean matches(NumericIndication in) {
        if (super.matches(in)) {
            if ((this.aggregation == null && in.getAggregation() == null) ||
                 (this.aggregation != null && this.aggregation.isEqual(in.getAggregation()))) {
                return true;
            }
        }
        
        return false;
    }
}
