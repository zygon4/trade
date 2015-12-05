/**
 * 
 */

package com.zygon.database.cassandra;

import com.zygon.database.PersistableComponent;
import me.prettyprint.hector.api.Serializer;

/**
 *
 * @author zygon
 */
public class CassandraPersistableComponent<T> extends PersistableComponent<T> {

    public final Serializer<T> serializer;

    public CassandraPersistableComponent(T key, Serializer<T> keySerializer) {
        super(key);
        this.serializer = keySerializer;
    }

    public Serializer<T> getSerializer() {
        return this.serializer;
    }
}
