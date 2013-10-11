/**
 * 
 */

package com.zygon.trade;

import java.io.File;
import java.sql.SQLException;
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

    @Deprecated
    private static Module[] findModules() {
        ModuleFinder moduleFinder = new ModuleFinder();
        return moduleFinder.getModules();
    }
    
    private final ConnectionManager connectionManager;
    private ConfigurationManager configurationManager;
    private ModuleSet moduleSet;
    
    private Module kernel;
    
    public Service() throws ClassNotFoundException {
        this.connectionManager = new ConnectionManager("org.apache.derby.jdbc.EmbeddedDriver");
    }
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        log.info(new Date(System.currentTimeMillis())+": Initializing");
        
        if (this.kernel != null) {
            throw new IllegalStateException("Kernel is already initialized");
        }
        
        // TODO: pass in root dir
        System.setProperty("trade.rootdir", File.pathSeparator + "home" + File.pathSeparator + 
                "zygon" + File.pathSeparator + "opt" + File.pathSeparator + "trade");
        
        // ConfigurationManager is a bump on a log right now.
        this.configurationManager = new ConfigurationManager(new DerbyStorage(this.connectionManager.getConnection()));
        
        this.moduleSet = new ModuleSet(this.configurationManager.getStorage());
        
        Module[] modules = this.moduleSet.getModules();
        this.moduleSet.configure();
        
        this.kernel = new Kernel(modules);
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
        
        try {
            this.connectionManager.close();
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
