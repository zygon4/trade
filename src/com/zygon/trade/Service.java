/**
 * 
 */

package com.zygon.trade;

import java.util.Date;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;

/**
 *
 * @author zygon
 */
public class Service implements Daemon {

    private static Module[] findModules() {
        ModuleFinder moduleFinder = new ModuleFinder();
        return moduleFinder.getModules();
    }
    
    private Module kernel;
    private Module[] modules;

    public Service(Module[] modules) {
        this.modules = modules;
    }

    public Service() {
        this(findModules());
    }
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println(new Date(System.currentTimeMillis())+": Initializing");
        
        if (this.kernel != null) {
            throw new IllegalStateException("Kernel is already initialized");
        }
        
        this.kernel = new Kernel(this.modules);
    }

    @Override
    public void start() throws Exception {
       System.out.println(new Date(System.currentTimeMillis())+": Starting");
       
       // Call privledged init method
       this.kernel.doInit();
    }

    @Override
    public void stop() throws Exception {
        System.out.println(new Date(System.currentTimeMillis())+": Stopping");
        
        // Call privledged uninit method
        this.kernel.doUninit();
    }

    @Override
    public void destroy() {
        System.out.println(new Date(System.currentTimeMillis())+": Destroying");
        this.kernel = null;
    }
}
