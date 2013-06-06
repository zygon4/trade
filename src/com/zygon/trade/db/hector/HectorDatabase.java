/**
 * 
 */

package com.zygon.trade.db.hector;

import com.zygon.trade.db.Database;
import com.zygon.trade.db.DatabaseMetadata;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

/**
 *
 * @author zygon
 */
public class HectorDatabase implements Database {
    
    private static final String CLUSTER_NAME = "cluster_name"; // Test Cluster
    private static final String COLUMN_FAMILY = "column_family";
    private static final String HOST = "host"; // "localhost:9160"
    private static final String KEYSPACE = "keyspace";
    
    private final CDAO cassDAO = new CDAO();
    // TODO: change cluster name - it's not really important but do so eventually.
    private Cluster cluster;
    
    @Override
    public void addStorage(DatabaseMetadata metaData) {
        
        // This should not be necessary - this may happen due to an intialization
        // race condition where the DB module is initialized later that those
        // who want to use it.
        if (this.cluster == null) {
            this.initialize(metaData);
        }
        
        String keyspaceName = metaData.getProperty(KEYSPACE);
        if (keyspaceName == null) {
            throw new IllegalArgumentException("keyspace required");
        }
        
        String columnFamilyName = metaData.getProperty(COLUMN_FAMILY);
        if (columnFamilyName == null) {
            throw new IllegalArgumentException("column family required");
        }
        
        KeyspaceDefinition keyspaceDesc = this.cluster.describeKeyspace(keyspaceName);
        if (keyspaceDesc == null) {
            keyspaceDesc = HFactory.createKeyspaceDefinition(keyspaceName);
            this.cluster.addKeyspace(keyspaceDesc, true);
        }
        
        HFactory.createKeyspace(keyspaceName, this.cluster);
        
        // Anything else? Column metadata?
    }

    @Override
    public String getName() {
        return "HectorDB";
    }
    
    @Override
    public void initialize(DatabaseMetadata metaData) {
        if (this.cluster == null) {
            
            String host = metaData.getProperty(HOST);
            if (host == null) {
                throw new IllegalArgumentException("host is required");
            }
                
            String clusterName = metaData.getProperty(CLUSTER_NAME);
            if (clusterName == null) {
                clusterName = "Test Cluster";
            }
            
            this.cluster = HFactory.createCluster(clusterName, new CassandraHostConfigurator(host), null);
        }
    }

    @Override
    public void uninitialize() {
        this.cluster.getConnectionManager().shutdown();
        this.cluster = null;
    }
}
