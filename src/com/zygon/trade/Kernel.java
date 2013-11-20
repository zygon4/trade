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

    private final String rootDirectory;

    public Kernel(Module[] modules) {
        super("Kernel");
        
        this.rootDirectory = System.getProperty("trade.rootdir");
        
        for (Module module : modules) {
            this.add(module);
        }
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
