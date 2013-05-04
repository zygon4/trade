/**
 * 
 */

package com.zygon.exchange.strategy;

/**
 *
 * @author zygon
 */
public interface Strategy<T_IN> {
    
    public static enum Advice {
        DO_NOTHING,
        BUY,
        SELL;
    }
    
    // keep it simple for now - make a StrategyResponse
    public Advice handle(T_IN in);
}
