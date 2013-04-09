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
public abstract class AbstractEventListener<FEED_TYPE, EVENT_TYPE> extends InformationBuffer<FEED_TYPE, EVENT_TYPE> {
    
    public AbstractEventListener(String name, Collection<InformationHandler<EVENT_TYPE>> eventProcessors) {
        super(name, eventProcessors);
        
    }

    protected abstract void handleFeed(FEED_TYPE item);
    
    @Override
    public final void handle(FEED_TYPE item) {
        
        // TODO: log
        
        this.handleFeed(item);
        
        // TODO: probably handle exceptions
    }
}
