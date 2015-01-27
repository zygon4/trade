
package com.zygon.configuration;

import com.zygon.trade.InstallableStorage;

/**
 *
 * @author zygon
 * 
 * 
 * TBD: Im not quite sure the full range of responsibility of this 
 * class is yet - it may go away, it may not.
 */
public class ConfigurationManager {
    
    private final InstallableStorage storage;

    public ConfigurationManager(InstallableStorage storage) {
        this.storage = storage;
    }
    
    public InstallableStorage getStorage() {
        return storage;
    }

}
