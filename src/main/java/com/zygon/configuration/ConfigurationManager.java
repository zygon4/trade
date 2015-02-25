
package com.zygon.configuration;

import com.google.common.base.Preconditions;
import com.zygon.schema.IntegerSchemaElement;
import com.zygon.schema.parse.ConfigurationSchema;
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
    
    // DB tables reference is kind of backwards - It would be nice to have
    // a more generic schema name/location vs "INSTALL"
    private static final String CONFIGURATION_TABLE_NAME = "INSTALL.CONFIGURATION";
    private static final String CONFIGURATION_TABLE_SCHEMA = 
            "(ID CHAR(1024) NOT NULL, CLASSNAME CHAR(1024) NOT NULL "
            + "PRIMARY KEY (ID), "
            + "CONSTRAINT ID_FK FOREIGN KEY (ID) REFERENCES INSTALLED (ID)";
    
    private final Connection con;
    private final String configurableId;
    
    private boolean installed = false;

    public ConfigurationManager(Connection con, String configurableId) {
        Preconditions.checkNotNull(con);
        Preconditions.checkNotNull(configurableId);
        
        this.con = con;
        this.configurableId = configurableId;
    }
    
    private void checkInstallation(Connection con) {
        
        if (!this.installed) {
            try {
                if (!DBUtil.tableExists(con, CONFIGURATION_TABLE_NAME)) {
                    DBUtil.createTable(con, CONFIGURATION_TABLE_NAME, CONFIGURATION_TABLE_SCHEMA);
                }
            } catch (SQLException sqe) {
                // log/do something better
                throw new RuntimeException(sqe);
            }
            this.installed = true;
        }
    }
    
    public Configuration getConfiguration() {
        this.checkInstallation(this.con);
        
        return new Configuration(new ConfigurationSchema(this.configurableId, "v1", new IntegerSchemaElement("count", "some important count", 50, 0, 100)));
    }
    
    public void store (Configuration configuration) {
        // TODO: persist
    }
}
