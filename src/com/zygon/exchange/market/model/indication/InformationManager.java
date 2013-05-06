/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.AbstractInformationHandler;
import java.util.Collection;

/**
 *
 * @author zygon
 * 
 * This is the main entry point for incoming data to be transformed into usable
 * information.
 * 
 * TODO: Provide status information and possibly pause/unpause capabilities.
 */
public final class InformationManager extends AbstractInformationHandler<Object> {
    
    private final Collection<IndicationListener> indicationListeners;
    
    public InformationManager(String name, Collection<IndicationListener> indicationListeners) {
        super(name);
        
        this.indicationListeners = indicationListeners;
    }
    
    @Override
    public void handle(Object t) {
        for (IndicationListener listener : this.indicationListeners) {
            listener.handle(t);
        }
    }
}
