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
        // Nothing to do yet
    }

    @Override
    public void uninitialize() {
        // Nothing to do yet
    }
}
