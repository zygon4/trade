/**
 * 
 */

package com.zygon.exchange.market.model.indication.numeric;

import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.InformationHandler;

/**
 *
 * @author zygon
 */
public class NumericIndicationBuilder {

    private final String security;
    private Aggregation aggregation;
    private Classification classification;
    private InformationHandler<Object> handler;

    public NumericIndicationBuilder(String security) {
        this.security = security;
    }

    public NumericIndicationBuilder set(Aggregation aggregation) {
        this.aggregation = aggregation;
        return this;
    }
    
    public NumericIndicationBuilder set(Classification classification) {
        this.classification = classification;
        return this;
    }

    public NumericIndicationBuilder set(InformationHandler<Object> handler) {
        this.handler = handler;
        return this;
    }

    public NumericIndicationListener build() {
        NumericIndicationListener indication = new NumericIndicationListener(this.security, this.classification, this.aggregation);
        if (this.handler != null) {
            indication.setHandler(this.handler);
        }
        return indication;
    }
}
