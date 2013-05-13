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
public abstract class IndicationListener<T_IN extends Indication> {
    
    private final String name;
    private final Logger log;
    private final Classification classification;
    
    public IndicationListener(String name, Classification classification) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.classification = classification;
    }

    public Classification getClassification() {
        return this.classification;
    }

    public String getName() {
        return this.name;
    }
    
    protected boolean matches(T_IN in) {
        if ((this.classification == null && in.getClassification() == null) ||
            (this.classification != null && this.classification.isEqual(in.getClassification()))) {
            return true;
        }
        
        return false;
    }
    
    protected abstract void doHandle(T_IN in);
    
    @Subscribe
    public void handle (T_IN in) {
        this.log.trace("Handling " + in);
        
        if (this.matches(in)) {
            this.log.debug("matched");
            this.doHandle(in);
        }
    }
}
