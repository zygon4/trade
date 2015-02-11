
package com.zygon.trade;

import com.zygon.configuration.Configuration;

/**
 *
 * @author david.charubini
 */
public class MetaData {
    
    private final String id;
    private final String clazz;
    private final Configurable configurable;
    private final Configuration configuration;

    public MetaData(String id, String clazz, Configurable configurable, Configuration configuration) {
        this.id = id;
        this.clazz = clazz;
        this.configurable = configurable;
        this.configuration = configuration;
    }

    public String getClazz() {
        return this.clazz;
    }

    public Configurable getConfigurable() {
        return this.configurable;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public String getId() {
        return this.id;
    }
}
