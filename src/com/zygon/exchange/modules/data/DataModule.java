/**
 * 
 */

package com.zygon.exchange.modules.data;

import com.zygon.exchange.Module;
import com.zygon.exchange.market.data.DataListener;

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
        this.listener.initalize();
    }

    @Override
    public void uninitialize() {
        this.listener.unintialize();
    }
}
