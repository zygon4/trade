/**
 * 
 */

package com.zygon.trade;

/**
 * This is the heart of the system. It holds all the Modules.
 * 
 * TBD: Add a shutdown command?
 *
 * @author zygon
 */
public class Kernel extends Module {

    private final Module[] modules;
    private final String rootDirectory;

    public Kernel(Module[] modules) {
        super("Kernel");
        this.modules = modules;
        
        this.rootDirectory = System.getProperty("trade.rootdir");
    }
    
    @Override
    public Module[] getModules() {
        return this.modules;
    }

    public String getRootDirectory() {
        return this.rootDirectory;
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
