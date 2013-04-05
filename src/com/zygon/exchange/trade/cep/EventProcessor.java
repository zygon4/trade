/**
 * 
 */

package com.zygon.exchange.trade.cep;

/**
 *
 * @author zygon
 */
public interface EventProcessor<EVENT_TYPE> {
    public void process (EVENT_TYPE event);
}
