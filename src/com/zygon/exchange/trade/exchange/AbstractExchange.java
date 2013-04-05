/**
 * 
 */

package com.zygon.exchange.trade.exchange;

/**
 *
 *  TBD: consider the pros/cons of not exposing the feed providers/distributors
 * to the outside world and instead carry out the activity of shuttling that data.
 * 
 * @author zygon
 */
public abstract class AbstractExchange<FEED_TYPE> implements Exchange<FEED_TYPE> {

    private final String name;

    public AbstractExchange(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
}
