
package com.zygon.trade;

import com.zygon.configuration.Configuration;
import com.zygon.trade.modules.core.PlaygroundModule;
import com.zygon.trade.modules.ui.CLIModule;
import java.util.HashMap;
import java.util.Map;

/**
 * For now this is an improved in-memory storage solution.
 *
 * @author zygon
 */
public class InMemoryInstallableStorageNEW implements InstallableStorage {

    private final Map<String, MetaData> metadataById = new HashMap<>();
    
    {
        CLIModule cliModule = new CLIModule("cli");
        Configuration cliConfig = new Configuration(cliModule.getSchema());
        cliConfig.setBooleanValue("enabled", true);
        this.metadataById.put(cliModule.getDisplayname(), 
                new MetaData(cliModule.getDisplayname(), "com.zygon.trade.modules.ui.CLIModule", cliConfig));
        
        this.metadataById.put("pg", 
                new MetaData("pg", 
                             PlaygroundModule.class.getCanonicalName(), 
                             new Configuration(new PlaygroundModule("foo").getSchema())));
    }
    
    @Override
    public String[] getStoredIds() {
        return this.metadataById.keySet().toArray(new String[this.metadataById.keySet().size()]);
    }

    @Override
    public MetaData retrieve(String id) {
        return this.metadataById.get(id);
    }

    @Override
    public void store(String id, MetaData metadata) {
        this.metadataById.put(id, metadata);
    }    
}
