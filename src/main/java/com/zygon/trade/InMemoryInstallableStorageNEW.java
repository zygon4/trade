
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
@Deprecated
public class InMemoryInstallableStorageNEW implements InstallableStorage {

    private Installable create(final InstallableMetaData metaData) {
        return new Installable() {

            @Override
            public String getId() {
                return metaData.getId();
            }

            @Override
            public InstallableMetaData getInstallableMetaData() {
                return metaData;
            }
        };
    }
    
    private final Map<String, Installable> metadataById = new HashMap<>();
    
    {
        CLIModule cliModule = new CLIModule("cli");
        Configuration cliConfig = cliModule.hasSchema() ? new Configuration(cliModule.getSchema()) : null;
        this.metadataById.put(cliModule.getDisplayname(), 
                create(InstallableMetaDataHelper.createServerMetaProperties(cliModule.getDisplayname(), cliConfig, "com.zygon.trade.modules.ui.CLIModule")));
        
        this.metadataById.put("pg", 
                create(InstallableMetaDataHelper.createServerMetaProperties(
                            "pg", 
                             new Configuration(new PlaygroundModule("foo").getSchema()), 
                             PlaygroundModule.class.getCanonicalName())));
    }
    
    @Override
    public String[] getStoredIds() {
        return this.metadataById.keySet().toArray(new String[this.metadataById.keySet().size()]);
    }

    @Override
    public Installable retrieve(String id) {
        return this.metadataById.get(id);
    }

    @Override
    public void store(Installable installable) {
        this.metadataById.put(installable.getId(), installable);
    }    
}
