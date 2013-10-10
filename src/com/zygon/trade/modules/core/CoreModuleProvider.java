/**
 * 
 */

package com.zygon.trade.modules.core;

import com.zygon.trade.Module;
import com.zygon.trade.ModuleProvider;
import com.zygon.trade.db.Database;
import com.zygon.trade.db.DatabaseFactory;
import com.zygon.trade.modules.data.DataModule;

/**
 *
 * @author zygon
 */
public class CoreModuleProvider implements ModuleProvider {

    private final Module[] modules;
    
    public CoreModuleProvider() {
        UIModule uiModule = new UIModule("UI");
        
        Database db = DatabaseFactory.get("com.zygon.trade.db.cassandra.CassandraDatabase");
        DBModule dbModule = new DBModule(db);
        
        DataModule dataModule = new DataModule();
        
        this.modules = new Module[]{ uiModule, dbModule, dataModule };
    }
    
    @Override
    public Module[] getModules() {
        return this.modules;
    }
}
