
package com.zygon.data.feed;

import com.zygon.data.Context;
import com.zygon.data.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author david.charubini
 */
public abstract class PollFeedAdapter<T> extends AbstractEventFeed<T> {

    private static final Logger logger = LoggerFactory.getLogger(PollFeedAdapter.class);
    
    private final class AdapterThread extends Thread {

        private volatile boolean running = true;
        
        public AdapterThread() {
            super(AdapterThread.class.getCanonicalName());
            setDaemon(false);
        }

        @Override
        public void run() {
            while (this.running) {
                try {
                    T t = PollFeedAdapter.this.get();
                    if (this.running) {
                        for (Handler<T> reg : PollFeedAdapter.this.getHandlers()) {
                            reg.handle(t);
                        }
                    }
                
                    if (PollFeedAdapter.this.cacheTime > 0) {
                        try {Thread.sleep(PollFeedAdapter.this.cacheTime);} catch (Throwable ignore) {}
                    }
                
                } catch (Exception e) {
                    logger.error(null, e);
                }
            }
        }
    }
    
    private boolean started = false;
    private AdapterThread runner = null;
    private final long cacheTime;
    
    public PollFeedAdapter(Context ctx, long cacheTime) {
        super(ctx); 
        this.cacheTime = cacheTime;
    }

    @Override
    public void register(Handler<T> reg) {
        if (!this.started) {
            this.runner = new AdapterThread();
            this.runner.start();
            this.started = true;
        }
        super.register(reg);
    }

    @Override
    public void unregister(Handler<T> reg) {
        super.unregister(reg);
        
        if (getHandlers().isEmpty()) {
            this.runner.running = false;
            this.runner.interrupt();
            this.runner = null;
            this.started = false;
        }
    }
    
    protected abstract T get();
}
