
package com.zygon.trade;

import com.google.common.collect.Maps;
import com.zygon.configuration.Configuration;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author zygon
 */
/*pkg*/ class ModuleSet {

    private final Map<Class<? extends Module>, ParentModule> parentByChildClass = Maps.newHashMap();
    private final Map<String, Module> modulesById = Maps.newHashMap();
    private final Map<String, Module> topLevelModulesById = Maps.newTreeMap();
    private final InstallableStorage installableStorage;

    public ModuleSet(InstallableStorage installableStorage) {
        this.installableStorage = installableStorage;
        this.loadModules();
    }
    
    void configure() {
        try {
            for (Entry<String, Module> entry : this.modulesById.entrySet()) {
                InstallableMetaData meta = this.installableStorage.retrieve(entry.getKey()).getInstallableMetaData();
                if (meta.hasConfiguration()) {
                    entry.getValue().configure(meta.getConfiguration());
                }
            }
        } catch (SQLException sql) {
            // TODO: log fatal error
            throw new RuntimeException(sql);
        }
    }
    
    /*pkg*/ Module[] getModules() {
        return this.topLevelModulesById.values().toArray(new Module[this.topLevelModulesById.size()]);
    }
    
    private void loadModules() {
        try {
            String[] moduleIds = this.installableStorage.getStoredIds();
        
            // Lets store the parent/child class relationships first
            for (String moduleId : moduleIds) {
                InstallableMetaDataHelper moduleMeta = new InstallableMetaDataHelper(this.installableStorage.retrieve(moduleId).getInstallableMetaData());
                if (!moduleMeta.getId().equals(moduleId)) {
                    //shouldn't happen - but nervous nelly - maybe remove this later
                    throw new IllegalArgumentException();
                }

                Module mod = null;

                try {
                    mod = Module.createModule(moduleMeta.getClazz(), moduleMeta.getId());
                } catch (Exception e) {
                    // Treat as fatal
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                if (mod instanceof ParentModule) {
                    ParentModule parentModule = (ParentModule) mod;

                    // TODO: load configuration for parent module

                    // Store our parent by child class relationship
                    this.parentByChildClass.put(parentModule.getChildClazz(), parentModule);
                    // Store the general id and module
                    this.modulesById.put(moduleMeta.getId(), parentModule);
                }
            }

            // Now, lets create any remaining children using their known parent
            for (String moduleId : moduleIds) {
                InstallableMetaDataHelper moduleMeta = new InstallableMetaDataHelper(this.installableStorage.retrieve(moduleId).getInstallableMetaData());
                if (!moduleMeta.getId().equals(moduleId)) {
                    //shouldn't happen - but nervous nelly - maybe remove this later
                    throw new IllegalArgumentException();
                }

                Module mod = null;

                // We are possibly re-creating a lot of classes here - we could
                // optimize if it becomes a problem.
                try {
                    mod = Module.createModule(moduleMeta.getClazz(), moduleMeta.getId());
                } catch (Exception e) {
                    // Treat as fatal
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }

                if (!(mod instanceof ParentModule)) {
                    Class<? extends Module> childClazz = (Class<? extends Module>) mod.getClass();

                    // This is not a parent class, does it *have* a parent?
                    if (this.parentByChildClass.containsKey(childClazz)) {
                        ParentModule knownParentModule = this.parentByChildClass.get(childClazz);

                        // TODO: load child configuration
                        Configuration config = moduleMeta.getConfiguration();

                        Module module = knownParentModule.createChild(config, false);
                        knownParentModule.add(module);
                    }

                    this.modulesById.put(moduleMeta.getId(), mod);
                }
            }

            // Modules which have no parents should be in the high-level collection
            // A module has no parent
            for (Entry<String,Module> module : this.modulesById.entrySet()) {
                if (!this.parentByChildClass.containsKey(module.getValue().getClass())) {
                    this.topLevelModulesById.put(module.getKey(), module.getValue());
                }
            }
        
        } catch (SQLException sql) {
            // TODO: log fatal error
            throw new RuntimeException(sql);
        }
    }
}
