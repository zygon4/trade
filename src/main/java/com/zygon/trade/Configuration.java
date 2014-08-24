
package com.zygon.trade;

import com.google.common.collect.Maps;
import com.zygon.schema.ConfigurationSchema;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class Configuration {

    // TBD: this is fucked now - need to rethink considering
    //      new configuration schema.
    
    private final Map<String, Property> propertiesByName = Maps.newHashMap();
    private final Map<String, String> valuesByName = Maps.newHashMap();
    
    private final ConfigurationSchema schema;

    public Configuration(ConfigurationSchema schema) {
        this.schema = schema;
        
        // TBD
//        for (Property prop : this.schema.getProperties()) {
//            this.propertiesByName.put(prop.getName(), prop);
//        }
    }

    public ConfigurationSchema getSchema() {
        return this.schema;
    }
    
    public String getValue(String name) {
//        if (!this.propertiesByName.containsKey(name)) {
//            throw new IllegalArgumentException();
//        }
        
        String val = this.valuesByName.get(name);
        
        if (val == null) {
            Property prop = this.propertiesByName.get(name);
            if (prop != null && prop.hasDefault()) {
                val = prop.getDefaultValue();
            }
        }
        
        return val;
    }
    
    public void setValue(String name, String value) {
//        if (!this.propertiesByName.containsKey(name)) {
//            throw new IllegalArgumentException();
//        }
        
        this.valuesByName.put(name, value);
    }
}
