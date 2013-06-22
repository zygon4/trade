/**
 * 
 */

package com.zygon.trade.modules.data;

import com.zygon.trade.Module;
import com.zygon.trade.db.Database;
import com.zygon.trade.market.data.AbstractDataProvider;
import com.zygon.trade.market.data.DataListener;
import com.zygon.trade.market.data.DataLogger;
import com.zygon.trade.market.data.DataProvider;
import com.zygon.trade.market.data.PersistentDataLogger;
import com.zygon.trade.modules.core.DBModule;

/**
 *
 * @author zygon
 */
public class DataModule extends Module {

    private final DataListener listener;
    
    public DataModule(String name, DataListener dataManager) {
        super(name);
        
        this.listener = dataManager;
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
        
        Database db = dbModule.getDatabase();
        
        // Boo hiss. These should be removed and a cleaner way to provide 
        // database access should be implemented.
        
        DataLogger dataLogger = this.listener.getDataLogger();
        if (dataLogger != null && dataLogger instanceof PersistentDataLogger) {
            ((PersistentDataLogger)dataLogger).setDatabase(db);
        }
        
        DataProvider dataProvider = this.listener.getDataProvider();
        if (dataProvider != null && dataProvider instanceof AbstractDataProvider) {
            ((AbstractDataProvider) this.listener.getDataProvider()).setDatabase(db);
        }
        
        this.listener.initalize();
    }

    @Override
    public void uninitialize() {
        this.listener.unintialize();
    }
}
