/**
 * 
 */

package com.zygon.exchange.trade.indication;

import com.zygon.exchange.trade.FeedHandler;


/**
 *
 * @author zygon
 */
public abstract class Indicator<INDICATION_TYPE, INPUT_TYPE> implements FeedHandler<INPUT_TYPE>{
    
//    private final int id;
//    private final String displayName;
//    private 
    
    public abstract INDICATION_TYPE get();
    
    /**
     * Dear god please make this safe internally. This is just a first attempt
     * at this..
     * 
     * @param input 
     */
    public abstract void set(INPUT_TYPE input);
}
