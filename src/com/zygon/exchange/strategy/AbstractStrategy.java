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
    
    protected abstract Response doHandle(T_IN in);
    
    @Override
    public Response handle(T_IN in) {
        this.log.trace("Handling: " + in);
        
        return this.doHandle(in);
    }

    protected Logger getLog() {
        return this.log;
    }
}
