/**
 * 
 */

package com.zygon.trade;

import com.google.common.collect.Sets;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;

import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.reflections.Reflections;
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
    
    private Collection<Module> findAllModules() {
        Reflections reflections = new Reflections("");
        Set<Class<? extends Module>> moduleClazzes = reflections.getSubTypesOf(Module.class);
        
        Set<Module> modules = Sets.newHashSet();
        
        for (Class<? extends Module> moduleClazz : moduleClazzes) {
            // Make sure this module is static or a parent module
            try {
                moduleClazz.getConstructor();
                modules.add(Module.createModule(moduleClazz.getCanonicalName()));
            } catch (NoSuchMethodException | SecurityException e) {
                // Ignore - this module is child-only
            } catch (ClassNotFoundException | IllegalAccessException | IllegalArgumentException | 
                     InstantiationException | InvocationTargetException e) {
                
                // log/do something better
                // For now we just bail - in the future it would be nice
                // to cleanly ignore some failures and march ahead.
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        
        return modules;
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
        
        // This is what is currently installed - it is expected that there are
        // modules in the classpath which represent these installed elements.
        LocalInstallableStorage localInstallableStorage = new LocalInstallableStorage(this.connectionManager.getConnection());
        
        // These are the modules on the classpath - some (maybe all, maybe none)
        // have been installed.  The not-installed (static or parents) ones
        // should be installed.
        // TODO: Get modules from classpath and give to ModuleSet
        Collection<Module> modulesInClasspath = findAllModules();
        
        for (Module moduleInClasspath : modulesInClasspath) {
            if (localInstallableStorage.retrieve(moduleInClasspath.getId()) == null) {
                localInstallableStorage.store(moduleInClasspath);
            }
        }
        
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
