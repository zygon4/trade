/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;
import com.zygon.exchange.AbstractInformationHandler;


/**
 *
 * @author zygon
 */

public abstract class Indication extends AbstractInformationHandler<Object> implements UpdateListener {
    
    private final String eventTypeName;
    private final String eventClassName;
    private final String statement;
    
    protected Indication (String name, String eventTypeName, String eventClassName, String statement) {
        super(name);
        this.eventTypeName = eventTypeName;
        this.eventClassName = eventClassName;
        this.statement = statement;
    }

    public String getEventClassName() {
        return this.eventClassName;
    }

    public String getEventTypeName() {
        return this.eventTypeName;
    }

    public String getStatement() {
        return this.statement;
    }
    
//      private INDICATION_TYPE indication;
//    
//    public INDICATION_TYPE get() {
//        return this.indication;
//    }

//    @Override
//    public void handle(Object t) {
//        
//    }

    protected Object translate(Object o) {
        return o;
    }
    
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents) {
        for (EventBean event : newEvents) {
            Object obj = translate(event.getUnderlying());
            System.out.println(obj);
            super.handle(obj);
        }
        
        for (EventBean event : oldEvents) {
            // TODO: log
        }
    }
}
