/**
 * 
 */

package com.zygon.exchange.trade;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
@Deprecated
public class FeedProcessor {
    
    private static final int EXEC_THREAD_POOL = 1;
    
    private static final class ExecTask implements Runnable {
        
        private final Map<FeedProvider<?>, FeedHandler<?>> distributorsByFeedProviders;

        public ExecTask(Map<FeedProvider<?>, FeedHandler<?>> distributorsByFeedProviders) {
            this.distributorsByFeedProviders = distributorsByFeedProviders;
            System.out.println("generating a NEW ExecTask");
        }
        
        @Override
        public void run() {
            for (FeedProvider provider : this.distributorsByFeedProviders.keySet()) {
                FeedHandler distributor = this.distributorsByFeedProviders.get(provider);
                distributor.handle(provider.get());
            }
        }
    }
    
    private final Map<FeedProvider<?>, FeedHandler<?>> distributorsByFeedProviders = Collections.synchronizedMap(new HashMap<FeedProvider<?>, FeedHandler<?>>());
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXEC_THREAD_POOL);
    
    public void register (FeedProvider<?> key, FeedHandler<?> val) {
        this.distributorsByFeedProviders.put(key, val);
    }
    
    public void unregister (FeedProvider<?> key) {
        this.distributorsByFeedProviders.remove(key);
    }
    
    public void initialize() {
        // TBD: better threading model
        this.executor.scheduleAtFixedRate(new ExecTask(this.distributorsByFeedProviders), 0, 15, TimeUnit.SECONDS);
    }
    
    public void uninitialize() {
        this.executor.shutdown();
    }
}
