/**
 * 
 */

package com.zygon.exchange;

/**
 *
 * @author zygon
 * 
 * This should not be limited to simply Ticker information, it could be news,
 * crazy-ass Twitter data, etc.
 */
public interface InformationProvider<FEED_TYPE> {
    public FEED_TYPE get();
}
