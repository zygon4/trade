/**
 * 
 */

package com.zygon.exchange.trade.indication.technical;

/**
 *
 * @author zygon
 */
public class SMA extends TechnicalIndicator {
    
    private final int window;

    public SMA(int window) {
        this.window = window;
    }
}
