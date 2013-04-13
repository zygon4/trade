/**
 * 
 */

package com.zygon.exchange.modules.trade;

import com.zygon.exchange.InformationBuffer;
import com.zygon.exchange.Module;
import com.zygon.exchange.ScheduledInformationProcessor;
import com.zygon.exchange.cep.AbstractEventListener;
import com.zygon.exchange.market.data.provider.DataProvider;
import com.zygon.exchange.market.model.indication.Indication;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public abstract class AbstractTradeModule2 extends Module {

    private final Collection<DataProvider> dataProviders;
    private final Collection<AbstractEventListener> eventListeners;
    private final Collection<Indication> indications;
    
    protected AbstractTradeModule2(String name, 
            Collection<DataProvider> dataProviders,
            Collection<AbstractEventListener> eventListeners,
            Collection<Indication> indications) {
        super(name);
        
        this.dataProviders = dataProviders;
        this.eventListeners = eventListeners;
        this.indications = indications;
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        InformationBuffer buffer = new InformationBuffer(getName()+"layer1", indications);
        for (AbstractEventListener listener : eventListeners) {
            listener.setHandler(buffer);
        }
        
        for (DataProvider provider : dataProviders) {
            for (AbstractEventListener listener : eventListeners) {
                ScheduledInformationProcessor.instance().register(provider, listener, provider.getInterval(), provider.getUnits());
            }
        }
    }

    @Override
    public void uninitialize() {
        // nothing to do yet
    }
}
