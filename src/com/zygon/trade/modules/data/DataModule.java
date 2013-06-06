/**
 * 
 */

package com.zygon.trade.modules.data;

import com.zygon.trade.Module;
import com.zygon.trade.db.DatabaseMetadata;
import com.zygon.trade.market.data.DataListener;
import com.zygon.trade.modules.core.DBModule;

/**
 *
 * @author zygon
 */
public class DataModule extends Module {

    private final DataListener listener;
    private final DatabaseMetadata dbMeta;
    
    public DataModule(String name, DataListener dataManager, DatabaseMetadata dbMeta) {
        super(name);
        
        this.listener = dataManager;
        this.dbMeta = dbMeta;
    }

    public DataListener getDataManager() {
        return this.listener;
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        DBModule dbModule = (DBModule) this.getModule(DBModule.ID);
        
        dbModule.createStorage(this.dbMeta);
        
        this.listener.initalize();
    }

    @Override
    public void uninitialize() {
        this.listener.unintialize();
    }
}
