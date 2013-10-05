/**
 *
 */
package com.zygon.trade.db.cassandra;

import com.zygon.trade.db.DataTransform;
import com.zygon.trade.db.Persistable;
import com.zygon.trade.db.Database;
import com.zygon.trade.db.PersistableComponent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.factory.HFactory;

/**
 *
 * @author zygon
 */
public class CassandraDatabase implements Database {

//    private static final Logger logger = LoggerFactory.getLogger(CassandraDatabase.class);
    private static final String COLUMN_FAMILY = "trade";
    
    private final Cluster cluster = HFactory.getOrCreateCluster("Test Cluster", "localhost:9160");
    private final Keyspace keyspace = HFactory.createKeyspace("trade", this.cluster);
    private final CassDAO cassDAO = new CassDAO(this.keyspace, COLUMN_FAMILY);
    
    private final Map<Class<?>, DataTransform> transformsByCls = new HashMap<>();

    @Override
    public void close() throws IOException {
    }

    @Override
    public String getName() {
        return "CassDB";
    }

    @Override
    public <K, N, V> void persist(Object obj) {

        DataTransform transform = this.transformsByCls.get(obj.getClass());
        
        if (transform != null) {
            Persistable<K, N, V> persistable = transform.transformTo(obj);
            
            // Silly hard cast for now
            CassandraPersistableComponent<K> k = (CassandraPersistableComponent) persistable.getKey();
            CassandraPersistableComponent<N> n = (CassandraPersistableComponent) persistable.getName();
            CassandraPersistableComponent<V> v = (CassandraPersistableComponent) persistable.getValue();
            
            K key = k.getComponent();
            Serializer<K> keySerializer = k.getSerializer();
            
            N name = n.getComponent();
            Serializer<N> nameSerializer = n.getSerializer();
            
            V value = v.getComponent();
            Serializer<V> valueSerializer = v.getSerializer();
            
            this.persist(key, keySerializer, name, nameSerializer, value, valueSerializer);
        }
        
        throw new IllegalStateException ("No transformer found for " + obj.getClass().getCanonicalName());
    }

    @Override
    public <K, N, V> Object retrieve(Class<?> cls, Persistable<K, N, V> key) {
        
        DataTransform transform = this.transformsByCls.get(cls);
        
        if (transform != null) {
            
            // Silly hard cast for now
            CassandraPersistableComponent<K> k = (CassandraPersistableComponent) key.getKey();
            CassandraPersistableComponent<N> n = (CassandraPersistableComponent) key.getName();
            CassandraPersistableComponent<V> v = (CassandraPersistableComponent) key.getValue();
            
            K cKey = k.getComponent();
            Serializer<K> keySerializer = k.getSerializer();
            
            N cName = n.getComponent();
            Serializer<N> nameSerializer = n.getSerializer();
            
            Serializer<V> valueSerializer = v.getSerializer();
            Object value = this.getColumn(cKey, cName, keySerializer, nameSerializer, valueSerializer);
            PersistableComponent val = new PersistableComponent(value);
            
            return transform.transformFrom(new Persistable<>(k, n, val));
        }
        
        throw new IllegalStateException ("No transformer found for " + cls.getCanonicalName());
    }

    private <K, N, V> V getColumn(K key, N column, Serializer<K> keySerializer, Serializer<N> nameSerializer, Serializer<V> valueSerializer) {
        return this.cassDAO.getColumn(key, column, keySerializer, nameSerializer, valueSerializer);
    }

    private <K, N, V> Map<N, V> getColumns(K key, Serializer<K> keySerializer, Serializer<N> nameSerializer, Serializer<V> valueSerializer) {
        return this.cassDAO.getColumns(key, keySerializer, nameSerializer, valueSerializer);
    }

    private <K, N, V> void persist(K key, Serializer<K> keySerializer, Map<N, V> cols, Serializer<N> nameSerializer, Serializer<V> valueSerializer) {
        this.cassDAO.persist(key, keySerializer, cols, nameSerializer, valueSerializer);
    }

    private <K, N, V> void persist(K key, Serializer<K> keySerializer, N name, Serializer<N> nameSerializer, V value, Serializer<V> valueSerializer) {
        this.cassDAO.persist(key, keySerializer, name, nameSerializer, value, valueSerializer);
    }

    public Map<Class<?>, DataTransform> getTransformsByCls() {
        return this.transformsByCls;
    }
}
