/**
 * 
 */

package com.zygon.trade;

import java.util.Date;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class Service implements Daemon {

    private static final Logger log = LoggerFactory.getLogger(Service.class);

    private static Module[] findModules() {
        ModuleFinder moduleFinder = new ModuleFinder();
        return moduleFinder.getModules();
    }
    
    private Module kernel;
    private final Module[] modules;

    public Service(Module[] modules) {
        this.modules = modules;
    }

    public Service() {
        this(findModules());
    }
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        log.info(new Date(System.currentTimeMillis())+": Initializing");
        
        if (this.kernel != null) {
            throw new IllegalStateException("Kernel is already initialized");
        }
        
        this.kernel = new Kernel(this.modules);
    }

    private final Object initLock = new Object();
    
    @Override
    public void start() throws Exception {
       log.info(new Date(System.currentTimeMillis())+": Starting");
       
       synchronized (this.initLock) {
           
           new Thread() {
               @Override
               public void run() {
                   // Call privileged init method
                   kernel.doInit();
               }
           }.start();
           
           // The main thread waits indefinitely
           initLock.wait();
       }
    }

    @Override
    public void stop() throws Exception {
        log.info(new Date(System.currentTimeMillis())+": Stopping");
       
        synchronized (this.initLock) {
            initLock.notify();
        }
        
        // Call privileged uninit method
        this.kernel.doUninit();
    }

    @Override
    public void destroy() {
        log.info(new Date(System.currentTimeMillis())+": Destroying");
        synchronized (this.initLock) {
            initLock.notifyAll();
        }
        this.kernel = null;
    }
}
