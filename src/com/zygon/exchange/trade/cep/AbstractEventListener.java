/**
 * 
 */

package com.zygon.exchange.trade.cep;

import com.zygon.exchange.trade.FeedHandler;

/**
 *
 * TBD: handle dynamic registration of event processors.
 * 
 * @author zygon
 */
public abstract class AbstractEventListener<FEED_TYPE, EVENT_TYPE> implements FeedHandler<FEED_TYPE> {
    
    private final EventProcessor<EVENT_TYPE>[] eventProcessors;

    public AbstractEventListener(EventProcessor<EVENT_TYPE>[] eventProcessors) {
        this.eventProcessors = eventProcessors;
    }

    protected final EventProcessor<EVENT_TYPE>[] getEventProcessors() {
        return this.eventProcessors;
    }
    
    protected abstract void handleFeed(FEED_TYPE item);
    
    @Override
    public final void handle(FEED_TYPE item) {
        
        // TODO: log
        
        this.handleFeed(item);
        
        // TODO: probably handle exceptions
    }
}
