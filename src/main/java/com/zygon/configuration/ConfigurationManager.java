
package com.zygon.configuration;

import com.google.common.base.Preconditions;
import com.zygon.util.DBUtil;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zygon
 * 
 * Idempotent Configuration Manager
 * 
 */
public final class ConfigurationManager {
    
    private static final String CONFIGURATION_TABLE_NAME = "CONFIGURATION";
    private static final String CONFIGURATION_TABLE_SCHEMA = 
            "(ID VARCHAR(1024) NOT NULL, CLASSNAME VARCHAR(1024) NOT NULL, "
            + "PRIMARY KEY (ID), "
            + "CONSTRAINT ID_FK FOREIGN KEY (ID) REFERENCES INSTALLED (ID))";
    
    private final Connection con;
    private final Configurable configurable;
    private final Configuration configuration;
    
    public ConfigurationManager(Connection con, Configurable configurable) {
        Preconditions.checkNotNull(con);
        Preconditions.checkNotNull(configurable);
        Preconditions.checkNotNull(configurable.getSchema());
        
        this.con = con;
        this.configurable = configurable;
        this.configuration = new Configuration(this.configurable.getSchema());
        
        this.installConfigurationResources(this.con);
    }
    
    private void installConfigurationResources(Connection con) {
        try {
            if (!DBUtil.tableExists(con, CONFIGURATION_TABLE_NAME)) {
                DBUtil.createTable(con, CONFIGURATION_TABLE_NAME, CONFIGURATION_TABLE_SCHEMA);
            }
        } catch (SQLException sqe) {
            // log/do something better
            throw new RuntimeException(sqe);
        }
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public boolean isConfigurableStored() {
        // TBD: does "configurableStored" mean that there's a spot open for
        // this configurable, or does it mean that all configuration content
        // has been configured?
        
        // TODO: query db
        return false;
    }
    
    public void store() {
        // TODO: persist
        
        // TODO: try/catch/rollback
        this.configurable.configure(this.getConfiguration());
    }
}
