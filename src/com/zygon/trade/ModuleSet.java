
package com.zygon.trade;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class ModuleSet {

    private final Map<String, Module> modulesById = new HashMap<>();
    private final InstallableStorage installableStorage;

    public ModuleSet(InstallableStorage installableStorage) {
        this.installableStorage = installableStorage;
        this.loadModules();
    }
    
    private Module createModule(MetaData metaData) 
            throws ClassNotFoundException, InstantiationException, IllegalAccessException, 
            NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        
        Class<Module> clazz = (Class<Module>) Class.forName(metaData.getClazz());
        Constructor<Module> constructor = null;
        Module newInstance = null;
        
        if (metaData.getConfigurable().getSchema() != null || 
            metaData.getConfigurable().getChildSchema() != null) {
            constructor = clazz.getConstructor(String.class, Schema.class, ChildSchema.class);
            newInstance = constructor.newInstance(metaData.getId(), metaData.getConfigurable().getSchema(), metaData.getConfigurable().getChildSchema());
        } else {
            constructor = clazz.getConstructor(String.class);
            newInstance = constructor.newInstance(metaData.getId());
        }
        
        return newInstance;
    }
    
    /*pkg*/ Module[] getModules() {
        return this.modulesById.values().toArray(new Module[this.modulesById.size()]);
    }
    
    private void loadModules() {
        String[] moduleIds = this.installableStorage.getStoredIds();
        
        for (String moduleId : moduleIds) {
            MetaData moduleMeta = this.installableStorage.retrieve(moduleId);
            if (!moduleMeta.getId().equals(moduleId)) {
                //shouldn't happen - but nervous nelly - maybe remove this later
                throw new IllegalArgumentException();
            }
            
            try {
                this.modulesById.put(moduleId, createModule(moduleMeta));
            } catch (Exception e) {
                // Not quite sure what to do - this is fatal
                throw new RuntimeException(e);
            }
        }
    }
}
