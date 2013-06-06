package com.zygon.trade.db.hector;


import me.prettyprint.cassandra.serializers.AsciiSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

/*pkg*/ final class CDAO {
    
    protected static final AsciiSerializer AsciiType = AsciiSerializer.get();
    protected static final BytesArraySerializer BytesType = BytesArraySerializer.get();
    protected static final CompositeSerializer CompositeType = CompositeSerializer.get();
    protected static final DateSerializer DateType = DateSerializer.get();
    protected static final LongSerializer LongType = LongSerializer.get();
    protected static final ObjectSerializer ObjectType = ObjectSerializer.get();
    protected static final StringSerializer UTF8Type = StringSerializer.get();

    public <K, N, V> void save(Keyspace keyspace, String columnFamily, 
            K key, Serializer<K> keySerializer, N col, Serializer<N> nameSerializer, V val, Serializer<V> valueSerializer) {
        Mutator<K> mutator = HFactory.createMutator(keyspace, keySerializer);
        HColumn<N, V> createColumn = HFactory.createColumn(col, val, nameSerializer, valueSerializer);
        mutator.insert(key, columnFamily, createColumn);
    }

    public <K, N, V> V getColumn(Keyspace keyspace, String columnFamily, 
            K key, N column, Serializer<K> keySerializer,
            Serializer<N> nameSerializer, Serializer<V> valueSerializer) {
        
        ColumnQuery<K, N, V> query = HFactory.createColumnQuery(keyspace,
                keySerializer, nameSerializer, valueSerializer);
        
        query.setColumnFamily(columnFamily);
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
}
