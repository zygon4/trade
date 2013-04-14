/**
 * 
 */

package com.zygon.exchange;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 *
 * @author zygon
 */
public class InformationBuffer<T_IN, T_OUT> extends AbstractInformationHandler<T_IN> {

    private static final class DispatchTask<T_IN, T_OUT> implements Runnable {

        private final InformationBuffer<T_IN, T_OUT> buffer;

        public DispatchTask(InformationBuffer<T_IN, T_OUT> buffer) {
            this.buffer = buffer;
        }
        
        @Override
        public void run() {
            Collection<InformationHandler<T_OUT>> targets = this.buffer.getTargets();
            
            if (targets != null) {
                T_IN element = null;
                while ((element = this.buffer.getQueue().poll()) != null) {

                    T_OUT out = this.buffer.translate(element);
                    
                    for (InformationHandler<T_OUT> handler : targets) {
                        handler.handle(out);
                    }
                }
            }
        }
    }
    
    private final Queue<T_IN> queue = new ConcurrentLinkedQueue<>();
    private final Executor service = Executors.newFixedThreadPool(2);
    private final Collection<InformationHandler<T_OUT>> targets;

    public InformationBuffer(String name, Collection<InformationHandler<T_OUT>> targets) {
        super(name);
        this.targets = targets;
    }

    private Queue<T_IN> getQueue() {
        return this.queue;
    }

    protected Collection<InformationHandler<T_OUT>> getTargets() {
        return this.targets;
    }

    @Override
    public void handle(T_IN t) {
        if (this.getTargets() != null) {
            if (this.queue.offer(t)) {
                this.service.execute(new DispatchTask(this));
            } else {
                // TBD: mark drop?
            }
        } else {
            // noone cares.. boo hoo
        }
    }
            
    protected T_OUT translate(T_IN in) {
        throw new UnsupportedOperationException("Child class must override this method");
    }
}
