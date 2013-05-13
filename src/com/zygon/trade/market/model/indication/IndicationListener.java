/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.google.common.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The purpose of an IndicationListener is to accept data from the lower 
 * layer and hand it to the interested parties.
 * 
 * @author zygon
 */
public abstract class IndicationListener<T_IN> {
    
    private final String name;
    private final Logger log;
    
    public IndicationListener(String name) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
    }

    public String getName() {
        return this.name;
    }
    
    @Subscribe
    public void handle (T_IN in) {
        this.log.trace("Handling " + in);
    }
}
