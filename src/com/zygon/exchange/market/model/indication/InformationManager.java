/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.zygon.exchange.AbstractInformationHandler;
import java.util.Collection;

/**
 *
 * @author zygon
 * 
 * TODO: Provide status information and possibly pause/unpause capabilities.
 */
public final class InformationManager extends AbstractInformationHandler<Object> {
    
    private final Collection<Indication> indications;
    private final EPRuntime runtime;
    
    public InformationManager(String name, Collection<Indication> indications) {
        super(name);
        
        this.indications = indications;
        
        Configuration cepConfig = new Configuration();
        
        for (Indication ind : this.indications) {
            cepConfig.addEventType(ind.getEventTypeName(), ind.getEventClassName());
        }
        
        EPServiceProvider cep = EPServiceProviderManager.getProvider(this.getName(), cepConfig);
        
        this.runtime = cep.getEPRuntime();
        
        for (Indication ind : this.indications) {
            EPStatement cepStatement = cep.getEPAdministrator().createEPL(ind.getStatement());
            cepStatement.addListener(ind);
        }
    }
    
    @Override
    public void handle(Object t) {
        this.runtime.sendEvent(t);
    }
}
