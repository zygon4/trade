package com.zygon.data;

/**
 *
 * @author davec
 */
public interface EventFeed<T> extends Feed<T> {

    public static interface Registration<R> {
        public void handle(R r);
    }
    
    public void register (Registration<T> reg);
    
    public void unregister (Registration<T> reg);
}
