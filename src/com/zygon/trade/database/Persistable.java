/**
 * 
 */

package com.zygon.trade.database;

/**
 * This is sort of meant to be generic but it's still in the shadow of nosql/
 * cassandra using a "key", a column "name", and a "value".  It's good enough
 * for now.
 *
 * @author zygon
 */
public class Persistable<K, N, V> {
    
    private final PersistableComponent<K> key;
    private final PersistableComponent<N> name;
    private final PersistableComponent<V> value;

    public Persistable(PersistableComponent<K> key, PersistableComponent<N> name, PersistableComponent<V> value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    public PersistableComponent<K> getKey() {
        return key;
    }

    public PersistableComponent<N> getName() {
        return name;
    }

    public PersistableComponent<V> getValue() {
        return value;
    }
}
