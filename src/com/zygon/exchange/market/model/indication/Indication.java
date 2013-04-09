/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.AbstractInformationHandler;


/**
 *
 * @author zygon
 */

public abstract class Indication<INDICATION_TYPE> extends AbstractInformationHandler<INDICATION_TYPE> {
    
    protected Indication (String name) {
        super(name);
    }
    
//    private final int id;
//    private final String displayName;
//    private 
    
    private INDICATION_TYPE indication;
    
    public INDICATION_TYPE get() {
        return this.indication;
    }

    @Override
    public void handle(INDICATION_TYPE t) {
        this.indication = t;
    }
}
