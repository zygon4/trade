/**
 * 
 */

package com.zygon.trade.db.cassandra;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.prettyprint.cassandra.serializers.AsciiSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.TimeUUIDSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

/**
 *
 * @author zygon
 */
public class CassDAO {

    public static final AsciiSerializer AsciiType = AsciiSerializer.get();
    public static final BytesArraySerializer BytesType = BytesArraySerializer.get();
    public static final CompositeSerializer CompositeType = CompositeSerializer.get();
    public static final DateSerializer DateType = DateSerializer.get();
    public static final LongSerializer LongType = LongSerializer.get();
    public static final DoubleSerializer DoubleType = DoubleSerializer.get();
    public static final ObjectSerializer ObjectType = ObjectSerializer.get();
    public static final StringSerializer UTF8Type = StringSerializer.get();
    public static final TimeUUIDSerializer TimeUUIDType = TimeUUIDSerializer.get();
    
    /**
     * Support for the "Valueless Column" design pattern.
     */
    public static final byte[] VALUELESS = new byte[0];
    
    private final String columnFamily;
    private final Keyspace keyspace;

    public CassDAO(Keyspace keyspace, String columnFamily) {
        
        if (keyspace == null || columnFamily == null) {
            throw new IllegalArgumentException("No null arguments permitted");
        }
        
        this.keyspace = keyspace;
        this.columnFamily = columnFamily;
    }

    public <K, N, V> V getColumn(K key, N column, Serializer<K> keySerializer,
            Serializer<N> nameSerializer, Serializer<V> valueSerializer) {

        ColumnQuery<K, N, V> query = HFactory.createColumnQuery(this.keyspace,
                keySerializer, nameSerializer, valueSerializer);
        
        query.setColumnFamily(this.columnFamily);
        query.setKey(key);
        query.setName(column);

        QueryResult<HColumn<N, V>> result = query.execute();
        HColumn<N, V> hcol = result.get();
        
        V value = null;
        
        if (hcol != null) {
            value = hcol.getValue();
        }
        
        return value;
    }
    
    public <K, N, V> Map<N, V> getColumns(K key, Serializer<K> keySerializer,
            Serializer<N> nameSerializer, Serializer<V> valueSerializer) {

        SliceQuery<K, N, V> query = HFactory.createSliceQuery(this.keyspace,
                keySerializer, nameSerializer, valueSerializer);
        
        query.setColumnFamily(this.columnFamily);
        query.setKey(key);
        
        QueryResult<ColumnSlice<N, V>> result = query.execute();
        ColumnSlice<N, V> hcol = result.get();
        
        Map<N, V> valuesByColName = null;
        
        if (hcol != null) {
            List<HColumn<N, V>> columns = hcol.getColumns();
            if (columns != null) {
                valuesByColName = new HashMap<N, V>();
                for (HColumn<N, V> col : columns) {
                    valuesByColName.put(col.getName(), col.getValue());
                }
            }
        }
        
        return valuesByColName;
    }
    
    public <K, N, V> void persist (K key, Serializer<K> keySerializer, 
                                   N name, Serializer<N> nameSerializer, 
                                   V value, Serializer<V> valueSerializer) {
        
        Mutator<K> mutator = HFactory.createMutator(this.keyspace, keySerializer);
        
        mutator.insert(key, this.columnFamily, 
                    HFactory.createColumn(name, value, nameSerializer, valueSerializer));
    }
    
    public <K, N, V> void persist (K key, Serializer<K> keySerializer, 
            Map<N, V> cols, Serializer<N> nameSerializer, Serializer<V> valueSerializer) {
        
        for (N name : cols.keySet()) {
            V value = cols.get(name);
            persist(key, keySerializer, name, nameSerializer, value, valueSerializer);
        }
    }
}
