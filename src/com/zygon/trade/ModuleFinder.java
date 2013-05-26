/**
 * 
 */

package com.zygon.trade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

/**
 *
 * @author zygon
 */
/*pkg*/ class ModuleFinder {
 
    private ModuleProvider[] getModuleProviders() {
        List<ModuleProvider> providers = new ArrayList<>();
        
        Reflections reflections = new Reflections(ClasspathHelper.forPackage("com.zygon.trade"), new SubTypesScanner());
        
        Set<Class<? extends ModuleProvider>> moduleProviders = reflections.getSubTypesOf(ModuleProvider.class);
        for (Class<? extends ModuleProvider> moduleProvider : moduleProviders) {
            ModuleProvider mp = null;
            
            try {
                mp = moduleProvider.getConstructor().newInstance();
            } catch (Exception e) { // too many f'in exceptions to catch individually..
                e.printStackTrace(); // dump nasty error to stderr and skip
                // TBD: log error
            }
            
            if (mp != null) {
                providers.add(mp);
            }
        }
        
        return providers.toArray(new ModuleProvider[providers.size()]);
    }
    
    public Module[] getModules() {
        List<Module> modules = new ArrayList<>();
        
        ModuleProvider[] moduleProviders = this.getModuleProviders();
        
        for (ModuleProvider provider : moduleProviders) {
            modules.addAll(Arrays.asList(provider.getModules()));
        }
        
        return modules.toArray(new Module[modules.size()]);
    }
}
