/**
 * 
 */

package com.zygon.exchange;

/**
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
    
    public abstract Module[] getModules();
    
    public final String getName() {
        return this.name;
    }
    
    public abstract void initialize();
    
    public abstract void uninitialize();
}
