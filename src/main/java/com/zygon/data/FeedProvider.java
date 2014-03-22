package com.zygon.data;

/**
 *
 * @author davec
 */
public interface FeedProvider {
    public <T> Feed<T> createFeed(Context ctx);
}
