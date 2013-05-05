/**
 * 
 */

package com.zygon.exchange;

import java.util.Collection;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class InformationBuffer<T_IN> extends AbstractInformationHandler<T_IN> {

    private static final class DispatchTask<T_IN> implements Runnable {

        private final InformationBuffer<T_IN> buffer;

        public DispatchTask(InformationBuffer<T_IN> buffer) {
            this.buffer = buffer;
        }
        
        @Override
        public void run() {
            Collection<InformationHandler<Object>> targets = this.buffer.getTargets();
            
            if (targets != null) {
                Object element = null;
                while ((element = this.buffer.getQueue().poll()) != null) {
                    for (InformationHandler<Object> handler : targets) {
                        handler.handle(element);
                    }
                }
            }
        }
    }
    
    // Consider using Guava publish/subscribe structure vs a plain queue.
    private final Queue<Object> queue = new ConcurrentLinkedQueue<>();
    private final Logger log;
    private Collection<InformationHandler<Object>> targets;
    private Executor service;
    
    public InformationBuffer(String name, Collection<InformationHandler<Object>> targets) {
        super(name);
        this.log = LoggerFactory.getLogger(name);
        this.targets = targets;
    }
    
    protected InformationBuffer(String name) {
        this(name, null);
    }

    private Queue<Object> getQueue() {
        return this.queue;
    }

    protected Collection<InformationHandler<Object>> getTargets() {
        return this.targets;
    }

    @Override
    public void handle(T_IN t) {
        if (this.getTargets() != null) {
            if (this.queue.offer(t)) {
                if (this.service != null) {
                    this.service.execute(new DispatchTask(this));
                } else {
                    new DispatchTask<>(this).run();
                }
            } else {
                this.log.warn(new Date(System.currentTimeMillis()) + ":" + this.getName()+ " dropped event");
                // TBD: mark drop?
            }
        } else {
            this.log.trace(new Date(System.currentTimeMillis()) + ":" + this.getName()+ " dropped event - no targets");
        }
    }

    public void setTargets(Collection<InformationHandler<Object>> targets) {
        this.targets = targets;
    }

    public final void setService(Executor service) {
        this.service = service;
    }
}
