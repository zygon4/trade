/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.zygon.exchange.AbstractInformationHandler;


/**
 *
 * @author zygon
 */

public abstract class IndicationListener<T extends Indication> extends AbstractInformationHandler<Object> implements StatementAwareUpdateListener {
    
    // how to make a default name??
//    private static String getName (Classification clazz, Aggregation aggregation, long duration, TimeUnit units) {
//        return aggregation.getType().name()+"_"+clazz.name()+"_"+duration+"_"+units.toString();
//    }
    
    protected IndicationListener (String name) {
        super(name);
    }
    
    public abstract String getStatement();
    
    public abstract T getReferenceIndication();
    
    protected Object translate(String name, Object o) {
        return o;
    }

    protected void newEvent(Object obj) {
        System.out.println(this.getName() + "[new]: "+ obj);
    }
    
    protected void oldEvent(Object obj) {
        System.out.println(this.getName() + "[old]: "+ obj);
    }
    
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement eps, EPServiceProvider epsp) {
        
//        for (EventBean event : newEvents) {
            Object obj = translate(eps.getServiceIsolated(), newEvents[0].getUnderlying());
//            epsp.getEPRuntime().sendEvent(eps);
            newEvent(obj);
//        }
        
//        for (EventBean event : oldEvents) {
//            oldEvent(event);
//        }
    }
}
