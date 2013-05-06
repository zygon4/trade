/**
 * 
 */

package com.zygon.exchange.strategy;

import com.zygon.exchange.AbstractInformationHandler;
import com.zygon.exchange.market.model.indication.Indication;

/**
 *
 * @author zygon
 */
public abstract class AbstractStrategy<T_IN extends Indication> extends AbstractInformationHandler<T_IN> implements Strategy<T_IN>{

    public AbstractStrategy(String name) {
        super(name);
    }
    
    protected abstract Advice getAdvice(T_IN in);
    
    protected abstract Evidence getEvidence(T_IN in);
    
    @Override
    public Response process(T_IN in) {
        this.getLog().trace("Handling: " + in);
        
        Advice advice = this.getAdvice(in);
        Evidence evidence = this.getEvidence(in);
        
        return new Response(advice, evidence);
    }
}
