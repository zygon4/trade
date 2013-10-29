
package com.zygon.trade.strategy.v2;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.strategy.TradeSummary;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author david.charubini
 */
public class Strategy {
    
    private final class StrategyThread extends Thread {

        private volatile boolean running = true;
        
        public StrategyThread() {
            super(StrategyThread.class.getCanonicalName());
            setDaemon(false);
        }

        @Override
        public void run() {
            while (this.running) {
                Message msg = Strategy.this.messageQueue.element();
                if (this.running) {
                    Strategy.this.process(msg);
                }
            }
        }
    }
    
    // TBD: The max size should be constrained and monitored.
    private final Queue<Message> messageQueue = new ArrayBlockingQueue<Message>(Integer.MAX_VALUE); 
    private final TradeSummary summary = new TradeSummary("tbd");
    private final Collection<Identifier> supportedIndicators;
    private final EventCriteria enterCriteria;
    private final EventCriteria exitCriteria;
    
    private StrategyThread runner = null;
    private boolean started = false;
    
    public Strategy(Collection<Identifier> supportedIndicators, EventCriteria enterCriteria, EventCriteria exitCriteria) {
        this.supportedIndicators = supportedIndicators;
        this.enterCriteria = enterCriteria;
        this.exitCriteria = exitCriteria;
    }
    
    public Collection<Identifier> getSupportedIndicators() {
        return this.supportedIndicators;
    }
    
    public TradeSummary getTradeSummary() {
        return this.summary;
    }
    
    private void process(Message msg) {
        // TODO: process enter/exit conditions
    }
    
    public void send(Message message) {
        this.messageQueue.add(message);
    }
    
    public void start() {
        if (!this.started) {
            this.runner = new StrategyThread();
            this.runner.start();
            this.started = true;
        }
    }
    
    public void stop() {
        if (this.started) {
            this.runner.running = false;
            this.runner.interrupt();
            this.runner = null;
            this.started = false;
        }
    }
}
