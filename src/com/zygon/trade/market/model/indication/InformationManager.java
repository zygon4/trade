/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.google.common.eventbus.EventBus;
import com.zygon.trade.AbstractInformationHandler;
import java.util.Collection;

/**
 *
 * @author zygon
 * 
 * This is the main entry point for incoming indications.
 * 
 * TODO: Provide status information and possibly pause/unpause capabilities.
 */
public final class InformationManager extends AbstractInformationHandler<Object> {
    
    private final Collection<IndicationListener> indicationListeners;
    private final EventBus eventBus;
    
    public InformationManager(String name, Collection<IndicationListener> indicationListeners) {
        super(name);
        
        this.indicationListeners = indicationListeners;
        this.eventBus = new EventBus(name);
    }
    
    @Override
    public void handle(Object t) {
        this.getLog().trace("Handling " + t);
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
