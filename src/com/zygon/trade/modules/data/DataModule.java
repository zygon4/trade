/**
 * 
 */

package com.zygon.trade.modules.data;

import com.zygon.trade.Module;
import com.zygon.trade.market.data.DataListener;
import com.zygon.trade.market.data.DataLogger;
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
        
        // Boo hiss. This should be removed and a cleaner way to provide 
        // database access should be implemented.
        DataLogger dataLogger = this.listener.getDataLogger();
        if (dataLogger != null && dataLogger instanceof PersistentDataLogger) {
            ((PersistentDataLogger)dataLogger).setDatabase(dbModule.getDatabase());
        }
        
        this.listener.initalize();
    }

    @Override
    public void uninitialize() {
        this.listener.unintialize();
    }
}
