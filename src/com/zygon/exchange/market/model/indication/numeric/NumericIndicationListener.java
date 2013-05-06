/**
 * 
 */

package com.zygon.exchange.market.model.indication.numeric;

import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.market.model.indication.IndicationListener;
import java.util.Collection;

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
            Classification classification, Collection<Interpreter<NumericIndication>> interpreters) {
        super(getName(classification, aggregation), tradeableIdentifier, interpreters);
        
        this.aggregation = aggregation;
    }
    
    public NumericIndicationListener (String security, Classification classification, Aggregation aggregation) {
        this(aggregation, security, classification, null);
    }

    public Aggregation getAggregation() {
        return aggregation;
    }
}
