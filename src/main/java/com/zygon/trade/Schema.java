
package com.zygon.trade;

import com.google.common.base.Preconditions;
import com.zygon.schema.parse.ConfigurationSchema;

/**
 * 
 * This class is split-brained for now. It used to take a schema resource (aka
 * a json schema representation), now it can also use a configuration schema
 * which contains an in-memory code representation of a schema.
 *
 * @author zygon
 * 
 */
public class Schema {

    private final String schemaResource;
    private final ConfigurationSchema configurationSchema;

    public Schema(String schemaResource, ConfigurationSchema configurationSchema) {
        Preconditions.checkArgument(schemaResource != null ^ configurationSchema != null);
        
        this.schemaResource = schemaResource;
        this.configurationSchema = configurationSchema;
    }
    
    public Schema(String schemaResource) {
        this (schemaResource, null);
    }
    
    public Schema(ConfigurationSchema configurationSchema) {
        this (null, configurationSchema);
    }

    public ConfigurationSchema getConfigurationSchema() {
        return this.configurationSchema;
    }

    public String getSchemaResource() {
        return this.schemaResource;
    }
}
