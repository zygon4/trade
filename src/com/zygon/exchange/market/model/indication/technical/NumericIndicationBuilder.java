/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import com.zygon.exchange.InformationHandler;
import com.zygon.exchange.market.model.indication.Indication;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class NumericIndicationBuilder {

    private final String security;
    private Aggregation aggregation;
    private Classification classification;
    private long duration;
    private TimeUnit units;
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

    public NumericIndicationBuilder set(long duration) {
        this.duration = duration;
        return this;
    }

    public NumericIndicationBuilder set(InformationHandler<Object> handler) {
        this.handler = handler;
        return this;
    }

    public NumericIndicationBuilder set(TimeUnit units) {
        this.units = units;
        return this;
    }
    
    public Indication build() {
        NumericIndication indication = new NumericIndication(this.security, this.classification, this.aggregation, this.duration, this.units);
        if (this.handler != null) {
            indication.setHandler(this.handler);
        }
        return indication;
    }
}
