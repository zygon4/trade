/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.strategy.IndicationProcessor;

/**
 *
 * @author zygon
 */
public class NumericIndicationBuilder {

    private Aggregation aggregation;
    private Classification classification;
    private IndicationProcessor<NumericIndication> handler;

    public NumericIndicationBuilder set(Aggregation aggregation) {
        this.aggregation = aggregation;
        return this;
    }
    
    public NumericIndicationBuilder set(Classification classification) {
        this.classification = classification;
        return this;
    }
    
    public NumericIndicationBuilder set(IndicationProcessor<NumericIndication> handler) {
        this.handler = handler;
        return this;
    }
    
    public NumericIndicationListener build() {
        NumericIndicationListener indication = new NumericIndicationListener(this.aggregation, this.classification, this.handler);
        return indication;
    }
}
