package com.zygon.trade;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.zygon.configuration.Configuration;
import com.zygon.configuration.ConfigurationManager;
import com.zygon.util.DBUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
            "(ID CHAR(1024) NOT NULL, CLASSNAME CHAR(1024) NOT NULL"
            + "PRIMARY KEY (ID))";
    
    private final Connection con;
    private boolean installed = false;

    public LocalInstallableStorage(Connection con) {
        Preconditions.checkNotNull(con);
        
        this.con = con;
    }
    
    private void checkInstallation(Connection con) {
        
        if (!this.installed) {
            try {
                if (!DBUtil.tableExists(con, INSTALLED_TABLE_NAME)) {
                    DBUtil.createTable(con, INSTALLED_TABLE_NAME, INSTALLED_TABLE_SCHEMA);
                }
            } catch (SQLException sqe) {
                // log/do something better
                throw new RuntimeException(sqe);
            }
            this.installed = true;
        }
    }
    
    private Module createModule(String clazz, String name) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        
        Class<Module> cls = (Class<Module>) Class.forName(clazz);
        Constructor<Module> constructor = null;
        Module newInstance = null;
        
        try {
            constructor = cls.getConstructor(String.class);
            newInstance = constructor.newInstance(name);
        } catch (NoSuchMethodException nsme) {
            // try again with parent module constructor signature
            
            constructor = cls.getConstructor();
            newInstance = constructor.newInstance();
        }
        
        return newInstance;
    }
    
    private static final String QUERY_GET_INSTALLED_IDS = "SELECT ID FROM " + INSTALLED_TABLE_NAME;
    
    @Override
    public String[] getStoredIds() throws SQLException {
        this.checkInstallation(this.con);
        
        List<String> storedIds = Lists.newArrayList();
        
        try (ResultSet results = DBUtil.executeQuery(this.con, QUERY_GET_INSTALLED_IDS)) {
            while (results.next()) {
                storedIds.add(results.getString(1));
            }
        }
        
        return storedIds.toArray(new String[storedIds.size()]);
    }
    
    private static String createRetrieveInstallableQuery(String id) {
        return String.format("SELECT * FROM %s WHERE ID = '%s'", INSTALLED_TABLE_NAME, id);
    }
    
    @Override
    public Installable retrieve(String id) throws SQLException {
        this.checkInstallation(this.con);
        
        try (ResultSet result = DBUtil.executeQuery(this.con, createRetrieveInstallableQuery(id))) {
            if (!result.next()) {
                // Not found - do a precheck?
                return null;
            }
            
            // 1) Instantiate class via its classpath
            String idCol = result.getString(1);
            String classnameCol = result.getString(2);
            
            try {
                Module installedModule = this.createModule(classnameCol, idCol);

                // 2) Check for configuration from Config Manager using id
                Configuration installedModuleConfiguration = new ConfigurationManager(this.con, installedModule).getConfiguration();

                return installedModule;                
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | 
                     InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
                // log/do something better
                throw new RuntimeException(ex);
            }
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
        this.checkInstallation(this.con);
        
        String id = new InstallableMetaDataHelper(installable.getInstallableMetaData()).getId();
        String clazz = new InstallableMetaDataHelper(installable.getInstallableMetaData()).getClazz();

        String storeSql = createStoreInstallable(id, clazz);
        DBUtil.executeUpdate(this.con, storeSql);
    }
}
