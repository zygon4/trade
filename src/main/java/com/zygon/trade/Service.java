/**
 * 
 */

package com.zygon.trade;

import java.io.File;
import java.sql.SQLException;

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

    private final ConnectionManager connectionManager;
    private ModuleSet moduleSet;
    
    private Module kernel;
    
    public Service() throws ClassNotFoundException {
        this.connectionManager = new ConnectionManager("org.apache.derby.jdbc.EmbeddedDriver");
    }
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        log.info("Initializing");
        
        if (this.kernel != null) {
            throw new IllegalStateException("Kernel is already initialized");
        }
        
        // TODO: pass in root dir
        System.setProperty("trade.rootdir", File.separator + "home" + File.separator + 
                "zygon" + File.separator + "opt" + File.separator + "trade");
        
        LocalInstallableStorage localInstallableStorage = new LocalInstallableStorage(this.connectionManager.getConnection());
        
        // TODO: Get modules from classpath and give to ModuleSet
        
        this.moduleSet = new ModuleSet(localInstallableStorage);
        
        Module[] modules = this.moduleSet.getModules();
        
        this.kernel = new Kernel(modules);
        
        this.kernel.setParents();
        this.moduleSet.configure();
        this.kernel.doHook();
    }

    private final Object initLock = new Object();
    
    @Override
    public void start() throws Exception {
       log.info("Starting");
       
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
        log.info("Stopping");
        
        synchronized (this.initLock) {
            initLock.notify();
        }
        
        // Call privileged uninit method
        this.kernel.doUninit();
        
        this.kernel.doUnHook();
    }

    @Override
    public void destroy() {
        log.info("Destroying");
        
        try {
            this.connectionManager.shutdown();
        } catch (SQLException e) {
            // Not sure what else to do..
            e.printStackTrace(System.err);
        }
        
        synchronized (this.initLock) {
            initLock.notifyAll();
        }
        this.kernel = null;
    }
}
