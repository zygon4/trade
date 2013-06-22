/**
 * 
 */

package com.zygon.trade.market.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * 
 * TODO: map multiple distributors to a single provider, perform state checks
 * when applicable.
 * 
 */
public class ScheduledDataProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(ScheduledDataProcessor.class);
    
    private static final class ExecTask implements Runnable {
        
        private final DataProvider provider;
        private final Set<DataListener> handlers;

        public ExecTask(DataProvider provider, Set<DataListener> handlers) {
            this.provider = provider;
            this.handlers = handlers;
        }

        @Override
        public void run() {
            if (this.provider.hasHistoricInformation()) {
                Collection historic = null;
                
                try {
                    historic = this.provider.getHistoric();
                } catch (Exception e) {
                    logger.error("Error getting historic data", e);
                }
                
                if (historic != null && !historic.isEmpty()) {
                    logger.info(new Date(System.currentTimeMillis()) + ": Loading historical data - " + historic.size() + " entries");
                    
                    
                    for (Object historicData : historic) {
                       for (DataListener handler : this.handlers) {
                            handler.handleHistoric(historicData);
                        }
                    }
                    
                    logger.info(new Date(System.currentTimeMillis()) + ": Done loading historical data");
                }
            } else {
                try {
                    Object val = this.provider.get();
                    for (DataListener handler : this.handlers) {
                        handler.handle(val);
                    }
                } catch (Exception e) {
                    logger.error("Error processing data", e);
                }
            }
        }
    }
    
    private final Map<DataProvider, Set<DataListener>> distributorsByInformationProviders = Collections.synchronizedMap(new HashMap<DataProvider, Set<DataListener>>());
    private final ScheduledExecutorService executor;

    public ScheduledDataProcessor(ScheduledExecutorService executor) {
        this.executor = executor;
    }
    
    public void register (DataProvider key, DataListener val, long interval, TimeUnit unit) {
        
        Set<DataListener> handlers = this.distributorsByInformationProviders.get(key);
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
