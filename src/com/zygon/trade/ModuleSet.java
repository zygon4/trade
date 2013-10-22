
package com.zygon.trade;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zygon
 */
/*pkg*/ class ModuleSet {

    private final Map<String, Module> modulesById = new HashMap<>();
    private final InstallableStorage installableStorage;

    public ModuleSet(InstallableStorage installableStorage) {
        this.installableStorage = installableStorage;
        this.loadModules();
    }
    
    void configure() {
        // TBD:
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
        
//        if (metaData.getConfigurable().getSchema() != null) {
//            constructor = clazz.getConstructor(String.class);
//            
//            // TBD: can't remember why I want to push schema to the constructor
//            // here.  If there' an upgrade from what's stored in the DB.. then
//            // upgrade first and then let the module get on with its life
//            // using its known schema..
//            
////            Schema schema = null;
////            if (metaData.getConfigurable().getSchema() != null) {
////                schema = new Schema(metaData.getConfigurable().getSchema().getSchemaResourceName());
////            }
//            
//            newInstance = constructor.newInstance(metaData.getId());
//        } else {
//            constructor = clazz.getConstructor(String.class);
//            newInstance = constructor.newInstance(metaData.getId());
//        } 
        
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
                Module module = createModule(moduleMeta);
                module.configure(moduleMeta.getConfigurable().getConfiguration());
                
                this.modulesById.put(moduleId, module);
            } catch (Exception e) {
                // Not quite sure what to do - this is fatal
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
