
package com.zygon.configuration;

import com.google.common.base.Preconditions;

/**
 * @author zygon
 * 
 * Idempotent Configuration Manager
 * 
 */
public final class ConfigurationManager {
    
    private final Configuration configuration;

    public ConfigurationManager(Configuration configuration) {
        Preconditions.checkNotNull(configuration);
        
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
}
