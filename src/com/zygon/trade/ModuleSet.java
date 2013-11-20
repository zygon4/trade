
package com.zygon.trade;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @author zygon
 */
/*pkg*/ class ModuleSet {

    private final Map<Class<? extends Module>, Module> parentByChildClass = new HashMap<Class<? extends Module>, Module>();
    private final Map<String, Module> modulesById = new HashMap<>();
    private final Map<String, Module> topLevelModulesById = new HashMap<>();
    private final InstallableStorage installableStorage;

    public ModuleSet(InstallableStorage installableStorage) {
        this.installableStorage = installableStorage;
        this.loadModules();
    }
    
    void configure() {
        for (Entry<String, Module> entry : this.modulesById.entrySet()) {
            MetaData meta = this.installableStorage.retrieve(entry.getKey());
            entry.getValue().configure(meta.getConfiguration());
        }
    }
    
    private Module createModule(MetaData metaData) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        
        Class<Module> clazz = (Class<Module>) Class.forName(metaData.getClazz());
        Constructor<Module> constructor = null;
        Module newInstance = null;
        
        // TBD: this got a little weird - not quite sure what constructors 
        // are required now and when..
        try {
            constructor = clazz.getConstructor(String.class);
            newInstance = constructor.newInstance(metaData.getId());
        } catch (NoSuchMethodException nsme) {
            // try again..
            
            constructor = clazz.getConstructor();
            newInstance = constructor.newInstance();
        }
        
        return newInstance;
    }
    
    /*pkg*/ Module[] getModules() {
        return this.topLevelModulesById.values().toArray(new Module[this.topLevelModulesById.size()]);
    }
    
    private Module create(MetaData moduleMeta, ParentModule parent) {
        try {
            Module module = null;
            if (parent != null) {
                module = parent.createChild(moduleMeta.getConfiguration(), false);
                parent.add(module);
            } else {
                module = createModule(moduleMeta);
            }
            
            this.modulesById.put(moduleMeta.getId(), module);
            
            return module;
        } catch (Exception e) {
            // Not quite sure what to do - this is fatal
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    private void loadModules() {
        String[] moduleIds = this.installableStorage.getStoredIds();
        
        // Lets store the parent/child class relationships first
        for (String moduleId : moduleIds) {
            MetaData moduleMeta = this.installableStorage.retrieve(moduleId);
            if (!moduleMeta.getId().equals(moduleId)) {
                //shouldn't happen - but nervous nelly - maybe remove this later
                throw new IllegalArgumentException();
            }
            
            if (moduleMeta.getConfigurable() instanceof ParentModule) {
                ParentModule parentModule = (ParentModule) moduleMeta.getConfigurable();
                Module module = this.create(moduleMeta, null);
                this.parentByChildClass.put(parentModule.getChildClazz(), module);
            }
        }
        
        // Now, lets create any remaining children using their known parent
        for (String moduleId : moduleIds) {
            MetaData moduleMeta = this.installableStorage.retrieve(moduleId);
            if (!moduleMeta.getId().equals(moduleId)) {
                //shouldn't happen - but nervous nelly - maybe remove this later
                throw new IllegalArgumentException();
            }
            
            if (!(moduleMeta.getConfigurable() instanceof ParentModule)) {
                Class<? extends Module> childClazz = (Class<? extends Module>) moduleMeta.getConfigurable().getClass();
                
                if (this.parentByChildClass.containsKey(childClazz)) {
                    ParentModule knownParentModule = (ParentModule) this.parentByChildClass.get(childClazz);
                    this.create(moduleMeta, knownParentModule);
                } else {
                    this.create(moduleMeta, null);
                }
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
