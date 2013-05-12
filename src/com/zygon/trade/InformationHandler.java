/**
 * 
 */

package com.zygon.trade;

/**
 * This is kind of a weak subscriber model.  The intention is to provide
 * a basic building block for pushing data up the stack (raw data -> useful
 * information -> market modeling -> trading strategies, etc)
 * 
 * @author zygon
 */
public interface InformationHandler<T> {
    public void handle (T t);
    
    // this should stay package scoped and a builder should be used to call this ONLY
    @Deprecated
    /*pkg*/ void setHandler(InformationHandler<T> handler);
}
