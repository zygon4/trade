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

    private static final String PRODUCT_NAME = "Stella";
    private static final String VERSION = "1.0";
    
    public static final String ID = "kernel";
    
    private final String rootDirectory;

    public Kernel(Module[] modules) {
        super(ID);
        
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

    @Override
    protected void doWriteStatus(StringBuilder sb) {
        sb.append(PRODUCT_NAME);
        sb.append(" - ");
        sb.append("v");
        sb.append(VERSION);
    }
}
