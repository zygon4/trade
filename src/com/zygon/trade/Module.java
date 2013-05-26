/**
 * 
 */

package com.zygon.trade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Module is intended to be a user facing element in the system. They hold
 * status information and any controllers that an operator might want to use
 * to interact with the underlying functionality.
 *
 * @author zygon
 */
public abstract class Module {
    
    private final String name;
    private final Logger logger;

    protected Module(String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(this.name);
    }
    
    /*pkg*/ void doInit() {
        
        this.logger.info("Initializing module {}", this.name);
        
        this.initialize();
    }
    
    /*pkg*/ void doUninit() {
        
        this.logger.info("Unintializing module {}", this.name);
        
        this.uninitialize();
    }

    protected final Logger getLogger() {
        return this.logger;
    }
    
    // TODO: perhaps someone should give a damn about this?  Or delete it.
    public abstract Module[] getModules();
    
    public final String getName() {
        return this.name;
    }
    
    public abstract void initialize();
    
    public abstract void uninitialize();
}
