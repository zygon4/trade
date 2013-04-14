/**
 * 
 */

package com.zygon.exchange;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

/**
 *
 * @author zygon
 */
public class Service implements Daemon {
    
    private final Module[] modules;

    public Service(Module[] modules) {
        this.modules = modules;
    }

    public Service() {
        this(new Module[]{});
    }
    
    private Module kernel;
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println("Initializing");
        
        // TODO: init modules from config
        
        if (this.kernel != null) {
            throw new IllegalStateException("Kernel is already initialized");
        }
        
        this.kernel = new Kernel(modules);
    }

    @Override
    public void start() throws Exception {
       System.out.println("Starting");
       this.kernel.initialize();
       
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stopping");
        this.kernel.uninitialize();
    }

    @Override
    public void destroy() {
        System.out.println("Destroying");
        this.kernel = null;
    }
}
