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
public abstract class Module implements OutputProvider {
    
    private final String name;
    private Module parent = null;
    private final Logger logger;

    protected Module(String name) {
        this.name = name;
        this.logger = LoggerFactory.getLogger(this.name);
    }
    
    /*pkg*/ void doInit() {
        
        Module[] modules = this.getModules();
        
        if (modules != null) {
            for (Module child : this.getModules()) {
                child.setParent(this);
            }
        }
        
        this.logger.info("Initializing module {}", this.name);
        
        boolean initialized = false;
        
        try {
            this.initialize();
            initialized = true;
        } catch (Exception e) {
            this.logger.error("Exception occurred while initializing module " + this.name, e);
        }
        
        if (initialized) {
            if (modules != null) {
                for (Module child : this.getModules()) {
                    child.doInit();
                }
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
    
    private Module getModule(Module module, String id) {
        if (module.name.equals(id)) {
            return module;
        } else {
            Module[] children = module.getModules();
            if (children != null) {
                Module mod = null;
                for (Module m : children) {
                    mod = getModule(m, id);
                    if (mod != null) {
                        break;
                    }
                }
                
                return mod;
            }
        }
        
        return null;
    }
    
    protected Module getModule(String id) {
        Module root = this.getRoot();
        return this.getModule(root, id);
    }
    
    public abstract Module[] getModules();
    
    @Override
    public final String getDisplayname() {
        return this.name;
    }

    @Override
    public String getNavigationElementToken() {
        return this.name.toLowerCase().replaceAll(" ", "");
    }

    @Override
    public Object getOutput(Request request) {
        return "";
    }

    public Module getParent() {
        return this.parent;
    }
    
    public final Module getRoot() {
        Module node = this;
        
        while (node.parent != null) {
            node = node.parent;
        }
        
        return node;
    }
    
    public abstract void initialize();

    private void setParent(Module parent) {
        this.parent = parent;
    }
    
    public abstract void uninitialize();
}
