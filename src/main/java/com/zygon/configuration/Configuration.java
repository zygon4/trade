
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
    
    public String getValue(String id) {
        Preconditions.checkArgument(this.valuesById.containsKey(id));
        return this.valuesById.get(id);
    }
    
    public void setValue(String id, String value) {
        Preconditions.checkArgument(this.valuesById.containsKey(id));
        this.valuesById.put(id, value);
    }
}
