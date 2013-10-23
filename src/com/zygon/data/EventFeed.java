package com.zygon.data;

/**
 *
 * @author davec
 */
public interface EventFeed<T> extends Feed<T> {

    public static interface Handler<R> {
        public void handle(R r);
    }
    
    public void register (Handler<T> reg);
    
    public void unregister (Handler<T> reg);
}
