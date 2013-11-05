package com.zygon.trade.agent;

import com.zygon.trade.trade.TradeSignal;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Identifier;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * TBD: Might turn this into its own package parallel to agent
 * TBD: This might just want to collapse and go away or merge into the signal
 *      generator.  I'm starting to doubt that we need both a Strategy and
 *      a Signal Generator.
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
                        Collection<TradeSignal> signals = Strategy.this.signalGenerator.getSignal(msg);
                        
                        if (!signals.isEmpty()) {
                            for (TradeSignal signal : signals) {
                                Strategy.this.outputQueue.put(signal);
                            }
                        }
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
    private final ArrayBlockingQueue<TradeSignal> outputQueue = new ArrayBlockingQueue<TradeSignal>(10000); // 10k is arbitrary
    
    private final String name;
    private final Collection<Identifier> supportedIndicators;
    private final SignalGenerator signalGenerator;
    
    private StrategyThread runner = null;
    private boolean started = false;
    
    public Strategy(
            String name, 
            Collection<Identifier> supportedIndicators, 
            SignalGenerator signalGenerator) {
        
        this.name = name;
        this.supportedIndicators = supportedIndicators;
        this.signalGenerator = signalGenerator;
    }

    public String getName() {
        return this.name;
    }
    
    public Collection<Identifier> getSupportedIndicators() {
        return this.supportedIndicators;
    }
    
    /**
     * Retrieves the available trade signals.
     * 
     * @param tradeSignals 
     * @param maxSignals T
     */
    void receive(Collection<TradeSignal> tradeSignals, int maxSignals) {
        // This (looking at the code) doesn't appear to block - yay!
        this.outputQueue.drainTo(tradeSignals, maxSignals);
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
