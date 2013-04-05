/**
 * 
 */

package com.zygon.exchange.trade;

/**
 *
 * @author zygon
 * 
 * This should not be limited to simply Ticker information, it could be news,
 * crazy-ass Twitter data, etc.
 */
public interface FeedProvider<FEED_TYPE> {
    public FEED_TYPE get();
}
