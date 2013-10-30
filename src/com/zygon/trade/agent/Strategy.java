package com.zygon.trade.agent;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.strategy.TradeSummary;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * TBD: might remove the trade summary and execution aspects in favor of just
 * handing off actionable trade signals to a queue for processing.
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
                Message msg = null;
                try {
                    msg = Strategy.this.messageQueue.take();
                    
                    if (this.running) {
                        Strategy.this.process(msg);
                    }
                } catch (InterruptedException ie) {
                    // TODO: logging
                    if (this.running) {
                        ie.printStackTrace();
                    }
                }
            }
        }
    }
    
    // TBD: The max size should be constrained and monitored.
    private final ArrayBlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(10000); // 10k is arbitrary
    
    // TBD: An output queue? So this class doesn't have to know about executing
    // orders. E.g. this generates orders and pushes them off.. but then it
    // doesn't know about trade summary.
    
    private final String name;
    private final Collection<Identifier> supportedIndicators;
    private final SignalGenerator signalGenerator;
    private final SignalHandler signalHandler;
    
    private StrategyThread runner = null;
    private boolean started = false;
    
    public Strategy(
            String name, 
            Collection<Identifier> supportedIndicators, 
            SignalGenerator signalGenerator,
            SignalHandler signalHandler) {
        
        this.name = name;
        this.supportedIndicators = supportedIndicators;
        this.signalGenerator = signalGenerator;
        this.signalHandler = signalHandler;
    }

    public String getName() {
        return this.name;
    }
    
    public Collection<Identifier> getSupportedIndicators() {
        return this.supportedIndicators;
    }
    
    private void process(Message msg) {
        
        // Big ole questions of where / how to wire up criteria/rules/implementation.
        // Should we have criteria or just a simple "get signal" interface?
        // If we do have a single interface that returns a signal.. should
        // we even process it here? or push that off to someone else?
        TradeSignal signal = this.signalGenerator.getSignal(msg);
        if (signal != TradeSignal.DO_NOTHING) {
            this.signalHandler.handle(signal);
        }
    }
    
    public void send(Message message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException ie) {
            // TODO: logging
            ie.printStackTrace();
        }
    }
    
    public void send(Collection<Message> messages) {
        for (Message msg : messages) {
            this.send(msg);
        }
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
