/**
 * 
 */

package com.zygon.trade;

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
