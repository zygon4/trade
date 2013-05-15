/**
 * 
 */

package com.zygon.trade.market.model.indication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * 
 * This is a super simple, basic "selector" for filtering out the indications
 * we care about. Currently
 * 
 * TODO: statistics..
 * 
 */
public class Selector<T_IN extends Indication> {

    private final Logger log;
    private final Identifier id;
    
    public Selector(Identifier id) {
        this.log = LoggerFactory.getLogger(Selector.class);
        
        if (id == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        
        this.id = id;
    }
    
    public boolean select (T_IN input) {
        
        if (this.id.equals(input.getId())) {
            this.log.debug(String.format("Input [%s] matched", input));
            return true;
        }
        
        this.log.debug(String.format("Input [%s] did not match", input));
        return false;
    }
}
