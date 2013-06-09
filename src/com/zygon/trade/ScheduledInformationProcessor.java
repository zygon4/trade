/**
 * 
 */

package com.zygon.trade;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    
    private static final class ExecTask implements Runnable {
        
        private final InformationProvider provider;
        private final Set<InformationHandler> handlers;

        public ExecTask(InformationProvider provider, Set<InformationHandler> handlers) {
            this.provider = provider;
            this.handlers = handlers;
        }

        @Override
        public void run() {
            if (this.provider.hasHistoricInformation()) {
                Collection historic = this.provider.getHistoric();
                if (!historic.isEmpty()) {
                    
                    System.out.println(new Date(System.currentTimeMillis()) + ": Loading historical data - " + historic.size() + " entries");
                    
                    for (Object historicData : historic) {
                       for (InformationHandler handler : this.handlers) {
                            handler.handle(historicData);
                        }
                    }
                    
                    System.out.println(new Date(System.currentTimeMillis()) + ": Done loading historical data");
                }
            } else {
                try {
                    Object val = this.provider.get();
                    for (InformationHandler handler : this.handlers) {
                        handler.handle(val);
                    }
                } catch (Exception e) {
                    // TODO: log
                    e.printStackTrace();
                }
            }
        }
    }
    
    private final Map<InformationProvider, Set<InformationHandler>> distributorsByInformationProviders = Collections.synchronizedMap(new HashMap<InformationProvider, Set<InformationHandler>>());
    private final ScheduledExecutorService executor;

    public ScheduledInformationProcessor(ScheduledExecutorService executor) {
        this.executor = executor;
    }
    
    public void register (InformationProvider key, InformationHandler val, long interval, TimeUnit unit) {
        
        Set<InformationHandler> handlers = this.distributorsByInformationProviders.get(key);
        if (handlers == null) {
            handlers = new HashSet<>();
            handlers.add(val);
            
            this.executor.scheduleAtFixedRate(new ExecTask(key, handlers), 0, interval, unit);
        } else {
            handlers.add(val);
        }
    }
    
    // TODO: unreg
}
