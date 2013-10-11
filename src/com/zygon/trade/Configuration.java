
package com.zygon.trade;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class Configuration {

    private final Map<String, Property> propertiesByName = new HashMap<>();
    private final Map<String, String> valuesByName = new HashMap<>();
    
    private final Schema schema;

    public Configuration(Schema schema) {
        this.schema = schema;
        
        for (Property prop : this.schema.getProperties()) {
            this.propertiesByName.put(prop.getName(), prop);
        }
    }

    public Schema getSchema() {
        return this.schema;
    }
    
    public String getValue(String name) {
        if (!this.propertiesByName.containsKey(name)) {
            throw new IllegalArgumentException();
        }
        
        String val = this.valuesByName.get(name);
        
        if (val == null) {
            Property prop = this.propertiesByName.get(name);
            if (prop.hasDefault()) {
                val = prop.getDefaultValue();
            }
        }
        
        return val;
    }
    
    public void setValue(String name, String value) {
        if (!this.propertiesByName.containsKey(name)) {
            throw new IllegalArgumentException();
        }
        
        this.valuesByName.put(name, value);
    }
}
