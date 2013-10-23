
package com.zygon.data.feed;

import com.zygon.data.Context;

/**
 *
 * @author david.charubini
 */
public abstract class PollFeedAdapter<T> extends AbstractEventFeed<T> {

    private final class AdapterThread extends Thread {

        private volatile boolean running = true;
        
        public AdapterThread() {
            super(AdapterThread.class.getCanonicalName());
            setDaemon(false);
        }

        @Override
        public void run() {
            while (this.running) {
                T t = PollFeedAdapter.this.get();
                if (this.running) {
                    for (Handler<T> reg : PollFeedAdapter.this.getHandlers()) {
                        reg.handle(t);
                    }
                }
                
                try {Thread.sleep(PollFeedAdapter.this.cacheTime);} catch (Throwable ignore) {}
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
            this.runner = null;
            this.started = false;
        }
    }
    
    protected abstract T get();
}
