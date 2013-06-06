/**
 * 
 */

package com.zygon.trade.modules.core;

import com.zygon.trade.Module;
import com.zygon.trade.db.Database;
import com.zygon.trade.db.DatabaseMetadata;

/**
 *
 * @author zygon
 */
public class DBModule extends Module {

    public static final String ID = DBModule.class.getCanonicalName();
    
    private final Database database;
    private final DatabaseMetadata dbMeta;
    
    public DBModule(Database database, DatabaseMetadata dbMeta) {
        super(ID);
        this.database = database;
        this.dbMeta = dbMeta;
    }
    
    public void createStorage(DatabaseMetadata metaData) {
        this.database.addStorage(metaData);
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }
    
    @Override
    public void initialize() {
        this.database.initialize(this.dbMeta);
    }

    @Override
    public void uninitialize() {
        this.database.uninitialize();
    }
}
