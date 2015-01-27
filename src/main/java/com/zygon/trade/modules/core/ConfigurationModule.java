
package com.zygon.trade.modules.core;

import com.zygon.configuration.ConfigurationManager;
import com.zygon.trade.Module;

/**
 *
 * @author zygon
 */
public class ConfigurationModule extends Module {

    public static final String ID = ConfigurationModule.class.getCanonicalName();
    
    private final ConfigurationManager configManager;
    
    public ConfigurationModule(ConfigurationManager configManager) {
        super(ID);
        
        this.configManager = configManager;
    }

    public ConfigurationManager getConfigManager() {
        return this.configManager;
    }
    
    @Override
    public void initialize() {
        // nothing to do
    }

    @Override
    public void uninitialize() {
        // nothing to do
    }
}
