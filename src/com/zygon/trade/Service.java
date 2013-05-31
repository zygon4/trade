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

    private final Object initLock = new Object();
    
    @Override
    public void start() throws Exception {
       System.out.println(new Date(System.currentTimeMillis())+": Starting");
       
       synchronized (this.initLock) {
           
           new Thread() {
               @Override
               public void run() {
                   // Call privledged init method
                   kernel.doInit();
               }
           }.start();
           
           // The main thread waits indefinitely
           initLock.wait();
       }
    }

    @Override
    public void stop() throws Exception {
        System.out.println(new Date(System.currentTimeMillis())+": Stopping");
       
        synchronized (this.initLock) {
            initLock.notify();
        }
        
        // Call privledged uninit method
        this.kernel.doUninit();
    }

    @Override
    public void destroy() {
        System.out.println(new Date(System.currentTimeMillis())+": Destroying");
        synchronized (this.initLock) {
            initLock.notifyAll();
        }
        this.kernel = null;
    }
}
