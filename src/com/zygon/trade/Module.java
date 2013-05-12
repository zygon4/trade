/**
 * 
 */

package com.zygon.trade;

/**
 * A Module is intended to be a user facing element in the system. They hold
 * status information and any controllers that an operator might want to use
 * to interact with the underlying functionality.
 *
 * @author zygon
 */
public abstract class Module {
    
    private final String name;

    protected Module(String name) {
        this.name = name;
    }
    
    /*pkg*/ void doInit() {
        
        System.out.println("initializing module " + this.name);
        
        this.initialize();
    }
    
    /*pkg*/ void doUninit() {
        
        System.out.println("Uninitializing module " + this.name);
        
        this.uninitialize();
    }
    
    // TODO: perhaps someone should give a damn about this?  Or delete it.
    public abstract Module[] getModules();
    
    public final String getName() {
        return this.name;
    }
    
    public abstract void initialize();
    
    public abstract void uninitialize();
}
