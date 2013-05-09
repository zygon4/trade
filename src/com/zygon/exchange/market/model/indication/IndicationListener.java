/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.AbstractInformationHandler;


/**
 * The purpose of an IndicationListener is to accept raw data from the lower 
 * layer and transform it into meaningful information.
 * 
 * @author zygon
 */
public abstract class IndicationListener<T extends Indication> extends AbstractInformationHandler<T> {
    
    private final String tradeableIdentifier;
    
    public IndicationListener(String name, String tradeableIdentifier) {
        super(name);
        
        this.tradeableIdentifier = tradeableIdentifier;
    }
    
    public String getTradeableIdentifier() {
        return this.tradeableIdentifier;
    }
}
