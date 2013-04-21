/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.market.model.indication.numeric.Numeric;
import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.zygon.exchange.AbstractInformationHandler;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author zygon
 * 
 * TODO: Provide status information and possibly pause/unpause capabilities.
 */
public final class InformationManager extends AbstractInformationHandler<Object> {
    
    private final Collection<IndicationListener> indications;
    private final EPRuntime runtime;
    
    private static final Class<Aggregation> AGGREGATION_CLS = Aggregation.class;
    private static final Class<Aggregation.Type> AGGREGATION_TYPE_CLS = Aggregation.Type.class;
    private static final Class[] DEFAULT_EVENT_TYPES = {AGGREGATION_CLS, AGGREGATION_TYPE_CLS};
    
    public InformationManager(String name, Collection<IndicationListener> indications) {
        super(name);
        
        this.indications = indications;
        
        Configuration cepConfig = new Configuration();
        
        for (Class cls : DEFAULT_EVENT_TYPES) {
            cepConfig.addEventType(cls);
        }
        
        for (IndicationListener ind : this.indications) {
            cepConfig.addEventType(ind.getEventClazz());
        }
        
        EPServiceProvider cep = EPServiceProviderManager.getProvider(this.getName(), cepConfig);
        
        this.runtime = cep.getEPRuntime();
        
        for (IndicationListener ind : this.indications) {
            EPStatement cepStatement = cep.getEPAdministrator().createEPL(ind.getStatement());
            cepStatement.addListener(ind);
        }
    }
    
    // I desprately want the the DataHandlers/InformationHandlers that mux the 
    // underlying incoming data to be here and not underneath.  I think this
    // manager should be able to choose its own path.
    @Override
    public void handle(Object t) {
        System.out.println("sending event " + t);
        this.runtime.sendEvent(t);
    }
}
