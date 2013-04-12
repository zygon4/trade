/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.xeiam.xchange.Currencies;
import com.zygon.exchange.AbstractInformationHandler;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
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
//            cepConfig.addImport("com.zygon.exchange.market.model.indication.Price");
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
    
    
    public static void main(String[] args) {
        List<Indication> indications = new ArrayList<>();
        indications.add(new AveragePrice(Currencies.USD, 10, TimeUnit.SECONDS));
        
        InformationManager mgmt = new InformationManager("info-mgmt", indications);
        
        Random rand = new Random(System.currentTimeMillis());
        
        for (int i = 0; i < 100; i++) {
            long price = rand.nextInt(50) + 50;
            Price p = new Price(Currencies.USD, price, System.currentTimeMillis() - (1000 * 60 * 10));
            mgmt.handle(p);
        }
    }
    
}
