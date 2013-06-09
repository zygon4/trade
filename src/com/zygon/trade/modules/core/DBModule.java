/**
 * 
 */

package com.zygon.trade.modules.core;

import com.zygon.trade.Module;
import com.zygon.trade.db.Database;
import java.io.IOException;

/**
 *
 * @author zygon
 */
public class DBModule extends Module {

    public static final String ID = DBModule.class.getCanonicalName();
    
    private final Database database;
    
    public DBModule(Database database) {
        super(ID);
        this.database = database;
    }

    // Really don't want to expose the full database out.. this is currently
    // being used for setting up persistent data logging
    public Database getDatabase() {
        return this.database;
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }
    
    @Override
    public void initialize() {
        // nothing to do
    }

    public <T> T retrieve(Class<T> cls, Object key) {
        return this.database.retrieve(cls, key);
    }
    
    public void store(Object object) {
        this.database.store(object);
    }
    
    @Override
    public void uninitialize() {
        try {
            this.database.close();
        } catch (IOException io) {
            this.getLogger().error("Error closing database " + this.database.getName(), io);
        }
    }
}
