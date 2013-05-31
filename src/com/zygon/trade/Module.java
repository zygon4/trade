/**
 * 
 */

package com.zygon.trade;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Module is intended to be a user facing element in the system. They hold
 * status information and any controllers that an operator might want to use
 * to interact with the underlying functionality.
 *
 * @author zygon
 */
public abstract class Module implements OutputProvider {
    
    private final String name;
    private final Logger logger;

    protected Module(String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(this.name);
    }
    
    /*pkg*/ void doInit() {
        
        this.logger.info("Initializing module {}", this.name);
        
        this.initialize();
        
        Module[] modules = this.getModules();
        if (modules != null) {
            for (Module child : this.getModules()) {
                child.doInit();
            }
        }
    }
    
    /*pkg*/ void doUninit() {
        
        this.logger.info("Unintializing module {}", this.name);
        
        Module[] modules = this.getModules();
        if (modules != null) {
            for (Module child : this.getModules()) {
                child.doUninit();
            }
        }
        
        this.uninitialize();
    }

    protected final Logger getLogger() {
        return this.logger;
    }
    
    public abstract Module[] getModules();
    
    public final String getName() {
        return this.name;
    }

    @Override
    public Object getOutput(Map<String, Object> input) {
        return "";
    }
    
    public abstract void initialize();
    
    public abstract void uninitialize();
}
