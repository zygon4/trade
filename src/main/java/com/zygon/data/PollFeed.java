package com.zygon.data;

/**
 *
 * @author davec
 */
public interface PollFeed<T> extends Feed<T> {
    public T get();
}
