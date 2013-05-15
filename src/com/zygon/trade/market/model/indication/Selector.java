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
 * we care about.
 * 
 * TODO: statistics..
 * 
 */
public class Selector<T_IN extends Indication> {

    private final Logger log;
    private final Identifier[] ids;
    
    public Selector(Identifier ...ids) {
        this.log = LoggerFactory.getLogger(Selector.class);
        
        if (ids == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        
        this.ids = ids;
    }
    
    public boolean select (T_IN input) {
        
        for (Identifier id : this.ids) {
            if (id.equals(input.getId())) {
                this.log.debug(String.format("Input [%s] matched ID[%s]", input, id));
                return true;
            }
        }
        
        this.log.debug(String.format("Input [%s] did not match", input));
        return false;
    }
}
