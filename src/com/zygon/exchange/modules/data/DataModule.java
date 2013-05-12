/**
 * 
 */

package com.zygon.exchange.modules.data;

import com.zygon.exchange.Module;
import com.zygon.exchange.market.data.DataManager;

/**
 *
 * @author zygon
 */
public class DataModule extends Module {

    private final DataManager dataManager;
    
    public DataModule(String name, DataManager dataManager) {
        super(name);
        
        this.dataManager = dataManager;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        this.dataManager.initalize();
    }

    @Override
    public void uninitialize() {
        this.dataManager.unintialize();
    }
}
