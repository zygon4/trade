
package com.zygon.trade;

import com.google.common.base.Preconditions;
import com.zygon.configuration.Configuration;
import java.util.Collections;
import java.util.Map;

/**
 *
 * This class helps store and retrieve installed objects
 * 
 * @author david.charubini
 */
public class InstallableMetaData {
    
    public static final String ID = "id";
    
    private final String id;
    private final Map<String,String> properties;
    private final Configuration configuration;

    public InstallableMetaData(String id, Configuration configuration, Map<String,String> properties) {
        Preconditions.checkNotNull(id);
        // Allowing configuration to be null
        Preconditions.checkNotNull(properties);
        
        this.id = id;
        this.configuration = configuration;
        this.properties = Collections.unmodifiableMap(properties);
    }
    
    public InstallableMetaData(String id, Map<String,String> properties) {
        this (id, null, properties);
    }
    
    public InstallableMetaData(String id, Configuration configuration) {
        this(id, configuration, Collections.EMPTY_MAP);
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    public String getId() {
        return this.id;
    }

    public String getProperty(String key) {
        return properties.get(key);
    }
    
    public boolean hasConfiguration() {
        return this.configuration != null;
    }
}
