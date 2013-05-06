/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.market.data.DataHandler;
import java.util.Collection;


/**
 * The purpose of an IndicationListener is to accept raw data from the lower 
 * layer and transform it into meaningful information.
 * 
 * @author zygon
 */
public abstract class IndicationListener<T extends Indication> extends DataHandler<T> {
    
    private final String tradeableIdentifier;
    
    public IndicationListener(String name, String tradeableIdentifier, Collection<Interpreter<T>> interpreters) {
        super(name, interpreters);
        
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
}
