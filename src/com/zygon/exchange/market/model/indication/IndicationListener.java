/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.google.common.eventbus.Subscribe;
import com.zygon.exchange.AbstractInformationHandler;


/**
 * The purpose of an IndicationListener is to accept raw data from the lower 
 * layer and transform it into meaningful information.
 * 
 * This is in shambles right now.. The data layer does transform the data into
 * indications but they really just hit a wall here.. should "listener" really
 * be a "processor"?  Does it need to exist at all?
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

    @Subscribe
    @Override
    public void handle(T t) {
        this.getLog().info("Handling " + t);
        super.handle(t);
    }
}
