/**
 * 
 */

package com.zygon.exchange.trade;

/**
 *
 * @author zygon
 */
public interface FeedHandler<FEED_TYPE> {
    public void handle(FEED_TYPE item);
}
