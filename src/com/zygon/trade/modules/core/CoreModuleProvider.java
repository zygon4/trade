/**
 * 
 */

package com.zygon.trade.modules.core;

import com.zygon.trade.Module;
import com.zygon.trade.ModuleProvider;
import com.zygon.trade.db.Database;
import com.zygon.trade.db.DatabaseFactory;
import com.zygon.trade.db.DatabaseMetadataImpl;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class CoreModuleProvider implements ModuleProvider {

    private final Module[] modules;
    
    public CoreModuleProvider() {
        UIModule uiModule = new UIModule("UI");
        
        DatabaseMetadataImpl impl = new DatabaseMetadataImpl();
        Map<String, String> options = impl.getProperties();
        
        options.put("cluster_name", "Test Cluster");
        options.put("host", "localhost:9160");
        Database db = DatabaseFactory.get("com.zygon.trade.db.hector.HectorDatabase");
        
        DBModule dbModule = new DBModule(db, impl);
        
        this.modules = new Module[]{ uiModule, dbModule };
    }
    
    @Override
    public Module[] getModules() {
        return this.modules;
    }
}
