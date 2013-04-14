/**
 * 
 */

package com.zygon.exchange.modules.data;

import com.zygon.exchange.Module;
import com.zygon.exchange.market.data.DataBridge;

/**
 *
 * @author zygon
 */
public class DataModule<T> extends Module {

    private final DataBridge<T> dataBridge;
    
    public DataModule(String name, DataBridge<T> dataBridge) {
        super(name);
        
        this.dataBridge = dataBridge;
    }

    protected final DataBridge<T> getDataBridge() {
        return this.dataBridge;
    }
    
    @Override
    public Module[] getModules() {
        return null;
    }

    @Override
    public void initialize() {
        this.dataBridge.initalize();
    }

    @Override
    public void uninitialize() {
        this.dataBridge.unintialize();
    }
}
