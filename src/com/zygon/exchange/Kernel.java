/**
 * 
 */

package com.zygon.exchange;

import java.util.ArrayList;

/**
 *
 * @author zygon
 */
public class Kernel extends Module {

    private final Module[] modules;

    public Kernel(Module[] modules) {
        super("Kernel");
        this.modules = modules;
    }
    
    private Module[] findModules() {
        ArrayList<Module> modules = new ArrayList<Module>();
        
        // TODO: reflectively load all the Modules - or use Spring
        
        return modules.toArray(new Module[modules.size()]);
    }
    
    @Override
    public Module[] getModules() {
        return this.modules;
    }

    @Override
    public void initialize() {
        for (Module module : this.modules) {
            module.doInit();
        }
    }

    @Override
    public void uninitialize() {
        for (Module module : this.modules) {
            module.doUninit();
        }
    }
}
