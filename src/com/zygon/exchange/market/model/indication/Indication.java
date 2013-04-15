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

public abstract class Indication extends AbstractInformationHandler<Object> implements StatementAwareUpdateListener {
    
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

    protected Object translate(String name, Object o) {
        return o;
    }

    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement eps, EPServiceProvider epsp) {
        for (EventBean event : newEvents) {
            Object obj = translate(eps.getServiceIsolated(), event.getUnderlying());
            System.out.println(this.getName() + ": "+ obj);
            handle(obj);
        }
        
        for (EventBean event : oldEvents) {
            // TODO: log
        }
    }
}
