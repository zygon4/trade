
package com.zygon.configuration;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.zygon.schema.parse.ConfigurationSchema;
import java.util.Map;

/**
 * TODO: values to/from persistence
 *
 * @author zygon
 */
public class Configuration {

    private final Map<String, String> valuesById = Maps.newHashMap();
    private final ConfigurationSchema schema;

    public Configuration(ConfigurationSchema schema) {
        Preconditions.checkNotNull(schema);
        
        this.schema = schema;
        this.valuesById.putAll(this.schema.getSchemaElements());
    }

    public ConfigurationSchema getSchema() {
        return this.schema;
    }
    
    public boolean getBooleanValue(String id) {
        return Boolean.parseBoolean(this.getValue(id));
    }
    
    public int getIntValue(String id) {
        return Integer.parseInt(this.getValue(id));
    }
    
    public long getLongValue(String id) {
        return Long.parseLong(this.getValue(id));
    }
    
    public String getStringValue(String id) {
        return this.getValue(id);
    }
    
    private String getValue(String id) {
        Preconditions.checkArgument(this.valuesById.containsKey(id));
        return this.valuesById.get(id);
    }
    
    public void resetDefaults() {
        // Clear shouldn't be necessary but doesn't hurt - especially
        // in case there's some bad data hanging around.
        this.valuesById.clear();
        this.valuesById.putAll(this.schema.getSchemaElements());
    }
    
    public void setBooleanValue(String id, boolean value) {
        this.setValue(id, String.valueOf(value));
    }
    
    public void setIntValue(String id, int value) {
        this.setValue(id, String.valueOf(value));
    }
    
    public void setLongValue(String id, long value) {
        this.setValue(id, String.valueOf(value));
    }
    
    public void setStringValue(String id, String value) {
        this.setValue(id, value);
    }
    
    private void setValue(String id, String value) {
        Preconditions.checkArgument(this.valuesById.containsKey(id));
        this.valuesById.put(id, value);
    }
}
