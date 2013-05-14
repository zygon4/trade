/**
 * 
 */

package com.zygon.trade.market.model.indication;

import com.google.common.eventbus.Subscribe;
import com.zygon.trade.strategy.IndicationProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The purpose of an IndicationListener is to accept data from the lower 
 * layer and hand it to the interested parties.
 * 
 * @author zygon
 */
public class IndicationListener<T_IN extends Indication> {
    
    private final String name;
    private final Logger log;
    private final Classification classification;
    private final IndicationProcessor<T_IN> processor;
    
    public IndicationListener(String name, Classification classification, IndicationProcessor<T_IN> processor) {
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.classification = classification;
        this.processor = processor;
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
    
    @Subscribe
    public void handle (T_IN in) {
        this.log.trace("Handling " + in);
        
        if (this.matches(in)) {
            this.log.debug("matched");
            
            if (this.processor != null) {
                IndicationProcessor.Response response = this.processor.process(in);
                
                this.log.debug("Indication Processor response: %s", response);
            }
        }
    }
}
