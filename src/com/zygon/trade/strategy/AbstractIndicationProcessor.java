/**
 * 
 */

package com.zygon.trade.strategy;

import com.zygon.trade.market.model.indication.Indication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public abstract class AbstractIndicationProcessor<T_IN extends Indication> implements IndicationProcessor<T_IN> {

    private final String name;
    private final Logger log;
    
    public AbstractIndicationProcessor (String name) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
    }
    
    protected abstract Advice getAdvice(T_IN in);
    
    protected abstract Evidence getEvidence(T_IN in);

    protected final Logger getLog() {
        return this.log;
    }
    
    // This may be dangerous..
    private volatile IndicationProcessor.Response response = null;
    
    @Override
    public void process(T_IN in) {
        this.log.trace("Handling: " + in);
        
        Advice advice = this.getAdvice(in);
        Evidence evidence = this.getEvidence(in);
        
        Response response = new Response(advice, evidence);
        
        if (this.response != null) {
            if (!this.response.isEquals(response)) {
                this.response = response;
                this.log.info("Indication Processor response: " + this.response);
            }
        } else {
            this.response = response;
            this.log.info("Indication Processor response: " + this.response);
        }
    }
}
