/**
 * 
 */

package com.zygon.trade.trade;

/**
 * DEPRECATED: replaced by TradeSignal for now
 *
 * @author zygon
 */
@Deprecated
public class Signal {
    
    private final String name;

    public Signal(String name) {
        
        if (name == null) {
            throw new IllegalArgumentException();
        }
        
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
