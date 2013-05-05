/**
 * 
 */

package com.zygon.exchange.strategy;

import com.zygon.exchange.market.model.indication.Indication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public abstract class AbstractStrategy<T_IN extends Indication> implements Strategy<T_IN>{

    private final String name;
    private final Logger log;
    
    public AbstractStrategy(String name) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
    }
    
    protected abstract Advice getAdvice(T_IN in);
    
    protected abstract Evidence getEvidence(T_IN in);
    
    protected Logger getLog() {
        return this.log;
    }
    
    @Override
    public Response handle(T_IN in) {
        this.log.trace("Handling: " + in);
        
        Advice advice = this.getAdvice(in);
        Evidence evidence = this.getEvidence(in);
        
        return new Response(advice, evidence);
    }
}
