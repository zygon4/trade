/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

/**
 *
 * @author zygon
 */
public class SMA extends TechnicalIndicator {
    
    private final int window;

    public SMA(int window) {
        super("SMA");
        this.window = window;
    }
}
