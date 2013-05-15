/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.google.common.eventbus.EventBus;
import com.zygon.trade.InformationHandler;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * 
 * This is the main entry point for incoming indications.
 * 
 * TODO: Provide status information and possibly pause/unpause capabilities.
 */
public final class InformationManager implements InformationHandler<Object> {
    
    private final String name;
    private final Logger log;
    private final Collection<IndicationListener> indicationListeners;
    private final EventBus eventBus;
    
    public InformationManager(String name, Collection<IndicationListener> indicationListeners) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.indicationListeners = indicationListeners;
        this.eventBus = new EventBus(name);
    }
    
    @Override
    public void handle(Object t) {
        this.log.trace("Handling " + t);
        this.eventBus.post(t);
    }
    
    public void initialize() {
        for (IndicationListener listener : this.indicationListeners) {
            this.eventBus.register(listener);
        }
    }
    
    public void uninitialize() {
        for (IndicationListener listener : this.indicationListeners) {
            this.eventBus.unregister(listener);
        }
    }
}
