package com.zygon.trade.agent;

import com.zygon.trade.trade.TradeGenerator;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.trade.TradePostMortem;
import com.zygon.trade.trade.TradeSummary;
import com.zygon.trade.trade.Trade;
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
                        Strategy.this.tradeGenerator.notify(msg);
                        
                        Collection<Trade> trades = Strategy.this.tradeGenerator.getTrades();
                        
                        if (!trades.isEmpty()) {
                            for (Trade trade : trades) {
                                Strategy.this.outputQueue.put(trade);
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
    private final ArrayBlockingQueue<Trade> outputQueue = new ArrayBlockingQueue<Trade>(10000); // 10k is arbitrary
    private final TradeSummary tradeSummary;
    private final String name;
    private final Collection<Identifier> supportedIndicators;
    private final TradeGenerator tradeGenerator;
    
    private StrategyThread runner = null;
    private boolean started = false;
    
    public Strategy(
            String name, 
            Collection<Identifier> supportedIndicators, 
            TradeGenerator signalGenerator) {
        
        this.name = name;
        this.supportedIndicators = supportedIndicators;
        this.tradeGenerator = signalGenerator;
        this.tradeSummary = new TradeSummary(name);
    }

    public String getName() {
        return this.name;
    }
    
    public Collection<Identifier> getSupportedIndicators() {
        return this.supportedIndicators;
    }

    public TradeSummary getTradeSummary() {
        return this.tradeSummary;
    }
    
    /**
     * Gives feedback to the strategy.
     * @param tradePostMortems 
     */
    public void process(Collection<TradePostMortem> tradePostMortems) {
        for (TradePostMortem tpm : tradePostMortems) {
            this.tradeSummary.add(tpm.getProfit());
        }
    }
    
    /**
     * Retrieves the available trades.
     * 
     * @param tradeSignals 
     * @param maxSignals T
     */
    void receive(Collection<Trade> tradeSignals, int maxSignals) {
        // This (looking at the code) doesn't appear to block - yay!
        this.outputQueue.drainTo(tradeSignals, maxSignals);
    }
    
    public void send(Message message) {
        try {
            this.messageQueue.put(message);
        } catch (InterruptedException ie) {
            // TODO: logging
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
