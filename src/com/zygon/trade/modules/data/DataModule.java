/**
 * 
 */

package com.zygon.trade.modules.data;

import com.zygon.trade.Module;
import com.zygon.trade.Property;
import com.zygon.trade.Schema;
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
    
    private static Schema CHILD_SCHEMA = new Schema(
            new Property[]{
                new Property("name"),
                new Property("feed-provider", new String[] {"BOX", "MTGOX"}),
                new Property("element", new String[] {"TRADE", "TICK"}),
                new Property("foo", "thedefault")
            });
    
    public DataModule(DataListener dataManager) {
        super("Data", null, CHILD_SCHEMA, null);
        
        this.listener = dataManager;
    }
    
    public DataModule() {
        this(null);
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
        
        this.listener.initialize();
    }

    @Override
    public void uninitialize() {
        this.listener.unintialize();
    }
}
