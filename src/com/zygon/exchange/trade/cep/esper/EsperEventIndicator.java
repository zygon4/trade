/**
 * 
 */

package com.zygon.exchange.trade.cep.esper;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.zygon.exchange.trade.cep.AbstractEventListener;
import com.zygon.exchange.trade.cep.EventProcessor;

/**
 *
 * Currently we have one runtime per listener.. i'm not sure what the best
 * practices will eventually be revealed to be..
 * 
 * @author zygon
 */
public abstract class EsperEventIndicator extends AbstractEventListener<Ticker, EventBean> implements UpdateListener {

    private final String statement;
    private final EPRuntime runtime;

    protected EsperEventIndicator(EventProcessor<EventBean>[] eventProcessors, Configuration config, String statement) {
        super(eventProcessors);
        
        EPServiceProvider cep = EPServiceProviderManager.getProvider("esperEventListener", config);
        
        this.statement = statement;
        this.runtime = cep.getEPRuntime();
        
        EPStatement cepStatement = cep.getEPAdministrator().createEPL(this.statement);
        cepStatement.addListener(this);
    }
    
    protected final EPRuntime getRuntime() {
        return this.runtime;
    }
    
    protected final String getStatement() {
        return this.statement;
    }

    @Override
    protected void handleFeed(Ticker item) {
        System.out.println("Handling item: " + item);
        this.runtime.sendEvent(item);
    }

    @Override
    public void update(EventBean[] newData, EventBean[] oldData) {
        for (EventProcessor<EventBean> processor : this.getEventProcessors()) {
            
            if (newData != null) {
                for (EventBean event : newData) {
                    processor.process(event);
                }
            }
            
            if (oldData != null) {
                for (EventBean event : oldData) {
                    processor.process(event);
                }
            }
        }
    }
}
