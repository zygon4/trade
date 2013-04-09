/**
 * 
 */

package com.zygon.exchange.cep;

import com.zygon.exchange.InformationBuffer;
import com.zygon.exchange.InformationHandler;
import java.util.Collection;

/**
 *
 * TBD: handle dynamic registration of event processors.
 * 
 * @author zygon
 */
public abstract class AbstractEventListener<T_IN, EVENT_TYPE> extends InformationBuffer<T_IN, EVENT_TYPE> {
    
    public AbstractEventListener(String name, Collection<InformationHandler<EVENT_TYPE>> eventProcessors) {
        super(name, eventProcessors);
    }
    
    // The effective purpose of this entire class now is to wrap this method..
    // is it worth it?
    protected abstract void handleFeed(T_IN item);
    
    @Override
    public final void handle(T_IN item) {
        
        // TODO: log
        
        this.handleFeed(item);
        
        // TODO: probably handle exceptions
    }
}
