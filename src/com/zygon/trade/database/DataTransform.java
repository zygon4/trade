
package com.zygon.trade.database;

/**
 * Responsible for translating objects into persistable items.
 *
 * @author zygon
 */
public interface DataTransform<T> {
    public <K, N, V> Persistable<K, N, V> transformTo(T object);
    public <K, N, V> T transformFrom(Persistable<K, N, V> val);
}
