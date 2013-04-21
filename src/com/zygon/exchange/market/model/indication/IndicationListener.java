/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.StatementAwareUpdateListener;
import com.zygon.exchange.AbstractInformationHandler;
import java.util.Collection;
import java.util.Iterator;


/**
 *
 * @author zygon
 */

public abstract class IndicationListener<T extends Indication> 
    extends AbstractInformationHandler<Object> implements StatementAwareUpdateListener {
    
    private final String tradeableIdentifier;
    private final Classification classification;
    private final Collection<IndicationListener<? extends Indication>> listeners;
    private final Class<? extends Indication> primaryEventClazz;
    private final boolean singleIndication;
    private String stmt = null;
    
    public IndicationListener(String name, String tradeableIdentifier, 
            Classification classification, Class<? extends Indication> primaryEventClazz, 
            Collection<IndicationListener<? extends Indication>> listeners) {
        super(name);
        
        this.tradeableIdentifier = tradeableIdentifier;
        this.classification = classification;
        this.listeners = listeners;
        this.primaryEventClazz = primaryEventClazz;
        this.singleIndication = listeners == null || listeners.isEmpty();
    }
    
    public IndicationListener (String name, String tradeableIdentifier, 
            Classification classification, Class<? extends Indication> clazz) {
        this(name, tradeableIdentifier, classification, clazz, null);
    }
    
    protected void appendFrom (StringBuilder sb, String clsName, String tradeableIdentifier, String classicationId) {
        sb.append(clsName);
        sb.append('(');
        
        sb.append("tradableIdentifier='").append(this.tradeableIdentifier).append('\'');
        sb.append(", id='").append(this.classification.getId()).append('\'');
        sb.append(')');
    }
    
    protected void appendFromStmt (StringBuilder sb, String label) {
        sb.append("from ");
        
        if (this.singleIndication) {
            appendFrom(sb, this.primaryEventClazz.getSimpleName(), this.getTradeableIdentifier(), this.getClassification().getId());
            if (label != null) {
                sb.append(" as ").append(label);
            }
        } else {
            Iterator<IndicationListener<? extends Indication>> iterator = this.listeners.iterator();
            
            while (iterator.hasNext()) {
                IndicationListener<? extends Indication> listener = iterator.next();
                listener.appendFrom(sb, listener.getEventClazz().getSimpleName(), listener.getTradeableIdentifier(), listener.getClassification().getId());
                sb.append(" as ").append(listener.getName());
                
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
        }
    }
    
    protected void appendSelectStmt(StringBuilder sb) {
        if (this.singleIndication) {
            sb.append("select tradableIdentifier, timestamp");
        } else {
            IndicationListener<? extends Indication> primaryListener = this.listeners.iterator().next();
            sb.append(String.format("select %s.tradableIdentifier, %s.timestamp", 
                    primaryListener.getName(), primaryListener.getName()));
        }
    }
    
    protected void createStatement(StringBuilder sb) {
        this.appendSelectStmt(sb);
        sb.append(' ');
        this.appendFromStmt(sb, this.getName());
    }

    public Classification getClassification() {
        return classification;
    }
    
    public String getStatement() {
        if (stmt == null) {
            StringBuilder sb = new StringBuilder();
            createStatement(sb);
            this.stmt = sb.toString();
        }
        
        return this.stmt;
    }

    public String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }

    // TBD: will probably need to expose a "getallclasses" method
    public Class<? extends Indication> getEventClazz() {
        return this.primaryEventClazz;
    }
    
    /**
     * Returns a new event to be injected into the Esper runtime, null if no
     * event is to be injected.
     * @param obj the latest event.
     * @return a new event to be injected into the Esper runtime, null if no
     * event is to be injected.
     */
    protected Object processNewEvent(Object obj) {
        System.out.println(this.getName() + "[new]: "+ obj);
        return null;
    }
    
    protected void processOldEvent(Object obj) {
        System.out.println(this.getName() + "[old]: "+ obj);
    }
    
    @Override
    public void update(EventBean[] newEvents, EventBean[] oldEvents, EPStatement eps, EPServiceProvider epsp) {
        
//        for (EventBean event : newEvents) {
            Object obj = newEvents[0].getUnderlying();

            Object newEvent = processNewEvent(obj);
            if (newEvent != null) {
                epsp.getEPRuntime().sendEvent(eps);
            }
//        }
        
//        for (EventBean event : oldEvents) {
//            oldEvent(event);
//        }
    }
}
