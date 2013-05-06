/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.AbstractInformationHandler;
import com.zygon.exchange.market.data.DataHandler;


/**
 * The purpose of an IndicationListener is to accept raw data from the lower 
 * layer and transform it into meaningful information.
 * 
 * @author zygon
 */
public abstract class IndicationListener<T extends Indication> extends DataHandler<Object> {
    
    private final String tradeableIdentifier;
    
    public IndicationListener(String name, String tradeableIdentifier) {
        super(name);
        
        this.tradeableIdentifier = tradeableIdentifier;
    }
    
    public String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }

    protected Object processNewEvent(Object obj) {
//        this.getLog()..getName() + "[new]: "+ obj);
        return null;
    }
    
    protected void processOldEvent(Object obj) {
//        System.out.println(this.getName() + "[old]: "+ obj);
    }

    @Override
    public void handle(Object t) {
        Object newEvent = processNewEvent(t);
        
    }
}
