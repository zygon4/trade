
package com.zygon.trade;

import com.google.common.collect.Maps;
import com.zygon.configuration.Configuration;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        for (Entry<String, Module> entry : this.modulesById.entrySet()) {
            InstallableMetaData meta = this.installableStorage.retrieve(entry.getKey()).getInstallableMetaData();
            entry.getValue().configure(meta.getConfiguration());
        }
    }
    
    private Module createModule(String clazz, String name) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        
        Class<Module> cls = (Class<Module>) Class.forName(clazz);
        Constructor<Module> constructor = null;
        Module newInstance = null;
        
        try {
            constructor = cls.getConstructor(String.class);
            newInstance = constructor.newInstance(name);
        } catch (NoSuchMethodException nsme) {
            // try again with parent module constructor signature
            
            constructor = cls.getConstructor();
            newInstance = constructor.newInstance();
        }
        
        return newInstance;
    }
    
    /*pkg*/ Module[] getModules() {
        return this.topLevelModulesById.values().toArray(new Module[this.topLevelModulesById.size()]);
    }
    
    private void loadModules() {
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
                mod = this.createModule(moduleMeta.getClazz(), moduleMeta.getId());
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
                mod = this.createModule(moduleMeta.getClazz(), moduleMeta.getId());
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
    }
}
