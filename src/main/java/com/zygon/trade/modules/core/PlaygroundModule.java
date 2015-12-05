
package com.zygon.trade.modules.core;

import com.zygon.configuration.Configuration;
import com.zygon.schema.IntegerSchemaElement;
import com.zygon.schema.parse.ConfigurationSchema;
import com.zygon.trade.Module;
import com.zygon.trade.Schema;

/**
 *
 * @author zygon
 */
public class PlaygroundModule extends Module {

    private static final String SCHEMA_ID_CONNECTIONS = "connections";
    
    private static Schema createSchema() {
        
        IntegerSchemaElement connections = new IntegerSchemaElement(SCHEMA_ID_CONNECTIONS, "like i said - important value", 20, 0, 100);
        ConfigurationSchema configSchema = new ConfigurationSchema(PlaygroundModule.class.getCanonicalName()+"_schema", "v1", connections);
        
        return new Schema(configSchema);
    }
    
    public PlaygroundModule(String name) {
        super(name, createSchema());
    }

    @Override
    public void configure(Configuration configuration) {
        super.configure(configuration);
        
        String connections = configuration.getStringValue(SCHEMA_ID_CONNECTIONS);
    }

    @Override
    public void initialize() {
        
    }

    @Override
    public void uninitialize() {
        
    }
}
