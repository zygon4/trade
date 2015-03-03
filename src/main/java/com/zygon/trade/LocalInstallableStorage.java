package com.zygon.trade;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zygon.configuration.ConfigurationManager;
import com.zygon.util.DBUtil;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

/**
 * Using Derby db
 *
 * @author david.charubini
 */
public class LocalInstallableStorage implements InstallableStorage {

    static {
	System.setProperty("derby.system.home", "/tmp");
    }
    
    private static final String INSTALLED_TABLE_NAME = "INSTALLED";
    private static final String INSTALLED_TABLE_SCHEMA = 
            "(ID VARCHAR(1024) NOT NULL, CLASSNAME VARCHAR(1024) NOT NULL, PRIMARY KEY (ID))";
    
    private final Connection con;
    private final Map<String,Module> installedModulesById = Maps.newHashMap();

    public LocalInstallableStorage(Connection con) {
        Preconditions.checkNotNull(con);
        
        this.con = con;
        
        this.installStorageResources(this.con);
    }
    
    private void installStorageResources(Connection con) {
        try {
            if (!DBUtil.tableExists(con, INSTALLED_TABLE_NAME)) {
                DBUtil.createTable(con, INSTALLED_TABLE_NAME, INSTALLED_TABLE_SCHEMA);
            }
        } catch (SQLException sqe) {
            // log/do something better
            throw new RuntimeException(sqe);
        }
    }
    
    private static final String QUERY_GET_INSTALLED_IDS = "SELECT ID FROM " + INSTALLED_TABLE_NAME;
    
    @Override
    public String[] getStoredIds() throws SQLException {
        
        List<String> storedIds = Lists.newArrayList();
        
        try (Statement stmt = con.createStatement()) {
            
            con.setAutoCommit(false);
            
            try (ResultSet results = stmt.executeQuery(QUERY_GET_INSTALLED_IDS)) {
                while (results.next()) {
                    storedIds.add(results.getString(1));
                }
            }
        }
        
        // TBD: cache-coherency checking
        
        return storedIds.toArray(new String[storedIds.size()]);
    }
    
    private static String createRetrieveInstallableQuery(String id) {
        return String.format("SELECT * FROM %s WHERE ID = '%s'", INSTALLED_TABLE_NAME, id);
    }

    @Override
    public void remove(String id) throws SQLException {
        // TODO: sql remove
        
        this.installedModulesById.remove(id);
    }
    
    @Override
    public Installable retrieve(String id) throws SQLException {

        String idCol = null;
        String classnameCol = null;
        
        try (Statement stmt = con.createStatement()) {
            con.setAutoCommit(false);
            
            try (ResultSet result = stmt.executeQuery(createRetrieveInstallableQuery(id))) {
                if (!result.next()) {
                    // Not found - do a precheck?
                    return null;
                }

                // 1) Instantiate class via its classpath
                idCol = result.getString(1);
                classnameCol = result.getString(2);
            }
        }
        
        try {
            Module installedModule = this.installedModulesById.get(id);

            if (installedModule == null) {
                installedModule = Module.createModule(classnameCol, idCol);

                // 2) Check for configuration from Config Manager using id
                if (installedModule.hasSchema()) {
                    ConfigurationManager configurationManager = new ConfigurationManager(this.con, installedModule);

                    if (!configurationManager.isConfigurableStored()) {
                        configurationManager.store();
                    }
                }

                this.installedModulesById.put(id, installedModule);
            }

            return installedModule;
        } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | 
                 InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            // log/do something better
            throw new RuntimeException(ex);
        }
    }

    private static String createStoreInstallable(String id, String clazz) {
        return String.format(
            "INSERT INTO %s (ID, CLASSNAME) " +
            "VALUES ('%s', '%s')",
            INSTALLED_TABLE_NAME, id, clazz
            );
    }
    
    @Override
    public void store(Installable installable) throws SQLException {
        String id = new InstallableMetaDataHelper(installable.getInstallableMetaData()).getId();
        String clazz = new InstallableMetaDataHelper(installable.getInstallableMetaData()).getClazz();

        String storeSql = createStoreInstallable(id, clazz);
        DBUtil.executeUpdate(this.con, storeSql);
    }
}
