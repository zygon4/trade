
package com.zygon.configuration;

import com.google.common.base.Preconditions;
import java.util.Collections;
import java.util.Map;

/**
 *
 * @author david.charubini
 */
public class MetaData {
    
    public static final String ID = "id";
    
    private final String id;
    private final Map<String,String> properties;
    private final Configuration configuration;

    public MetaData(String id, Configuration configuration, Map<String,String> properties) {
        Preconditions.checkNotNull(id);
        Preconditions.checkNotNull(configuration);
        Preconditions.checkNotNull(properties);
        
        this.id = id;
        this.configuration = configuration;
        this.properties = Collections.unmodifiableMap(properties);
    }
    
    public MetaData(String id, Configuration configuration) {
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
}
