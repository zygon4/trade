/**
 * 
 */

package com.zygon.exchange;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 * 
 * TODO: map multiple distributors to a single provider, perform state checks
 * when applicable.
 * 
 */
public class ScheduledInformationProcessor {
    
    private static ScheduledInformationProcessor processor = new ScheduledInformationProcessor();
    
    public static ScheduledInformationProcessor instance() {
        return processor;
    }
    
    private static final int EXEC_THREAD_POOL = 3;
    
    private static final class ExecTask implements Runnable {
        
        private final InformationProvider provider;
        private final Set<InformationHandler> handlers;

        public ExecTask(InformationProvider provider, Set<InformationHandler> handlers) {
            this.provider = provider;
            this.handlers = handlers;
            System.out.println("generating a NEW ExecTask");
        }

        @Override
        public void run() {
            Object val = this.provider.get();
            for (InformationHandler handler : this.handlers) {
                handler.handle(val);
            }
        }
    }
    
    private final Map<InformationProvider, Set<InformationHandler>> distributorsByInformationProviders = Collections.synchronizedMap(new HashMap<InformationProvider, Set<InformationHandler>>());
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXEC_THREAD_POOL);

    private ScheduledInformationProcessor() { }
    
    public void register (InformationProvider key, InformationHandler val, long interval, TimeUnit unit) {
        
        Set<InformationHandler> handlers = this.distributorsByInformationProviders.get(key);
        if (handlers == null) {
            handlers = new HashSet<>();
            handlers.add(val);
            
            this.executor.scheduleAtFixedRate(new ExecTask(key, handlers), interval, interval, unit);
        } else {
            handlers.add(val);
        }
    }
    
    // TODO: unreg
    
    public void initialize() {
        // TBD: better threading model
    }
    
    public void uninitialize() {
        this.executor.shutdown();
    }
}
