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
public class DataModule<T> extends Module {

    private final DataManager<T> dataManager;
    
    public DataModule(String name, DataManager<T> dataManager) {
        super(name);
        
        this.dataManager = dataManager;
    }

    public DataManager<T> getDataManager() {
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
