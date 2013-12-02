
package com.zygon.trade.agent;

import com.zygon.data.Handler;
import com.zygon.data.RawDataWriter;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.trade.TradePostMortem;
import com.zygon.trade.trade.Trade;
import com.zygon.trade.trade.TradeBroker;
import com.zygon.trade.trade.TradeSummary;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ArrayBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Right now an Agent encompasses a few things: receiving raw information
 * from the exchanges (e.g. tickers), interpreting the information into
 * messages, analyzing the messages to form trading signal, and finally,
 * processing the signals by translating them into orders.
 *
 * @author zygon
 * 
 * TODO: heterogeneous data 
 */
public class Agent<T> implements Handler<T> {

    private final class AgentThread extends Thread {

        private volatile boolean running = true;
        
        public AgentThread() {
            super(AgentThread.class.getCanonicalName());
            setDaemon(false);
        }

        @Override
        public void run() {
            while (this.running) {
                
                T data = null;
                
                try {
                    data = Agent.this.dataQueue.take();
                    
                    if (Agent.this.dataWriter != null) {
                        try {
                            Agent.this.dataWriter.log(data);
                        } catch (IOException io) {
                            if (this.running) {
                                // TBD: anything else we can do? We'd probably like
                                // to alarm/alert here.
                                Agent.this.log.error(null, io);
                            }
                        }
                    }
                    
                    if (this.running) {
                        // 1) interpret data
                        Collection<Message> messages = Agent.this.interpretData(data);

                        // 2) process information
                        if (!messages.isEmpty()) {
                            Agent.this.processInformation(messages);
                        }
                        
                        // 3) take actions (if there are any)
                        Agent.this.processTradeSignals();
                        
                        // 4) give feedback to the strategy (if there is any)
                        Agent.this.processPostTrade();
                    }
                } catch (ExchangeException ee) {
                    if (this.running) {
                        // TBD: anything else we can do? We'd probably like
                        // to alarm/alert here.
                        Agent.this.log.error(null, ee);
                    }
                } catch (InterruptedException ie) {
                    if (this.running) {
                        Agent.this.log.error(null, ie);
                    }
                }
            }
        }
    }
    
    private final ArrayBlockingQueue<T> dataQueue = new ArrayBlockingQueue<T>(10000); // 10k is arbitrary
    private final String name;
    private final Logger log;
    private final Collection<Interpreter<T>> interpreters;
    private final Strategy strategy;
    
    private TradeBroker broker;
    private AgentThread runner = null;
    private boolean started = false;
    private RawDataWriter<T> dataWriter = null;
    
    public Agent(String name, Collection<Interpreter<T>> interpreters, Strategy strategy, TradeBroker broker) {
        
        if (name == null || interpreters == null || strategy == null) {
            throw new IllegalArgumentException("No null arguments permitted");
        }
        
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.interpreters = interpreters;
        this.strategy = strategy;
        this.broker = broker;
    }
    
    public TradeSummary getStrategySummary() {
        return this.strategy.getTradeSummary();
    }
    
    @Override
    public void handle(T t) {
        if (t != null) {
            try {
                this.dataQueue.put(t);
            } catch (InterruptedException ie) {
                this.log.error(null, ie);
            }
        } else {
            this.log.debug("Received null data");
        }
    }
    
    public void initialize() {
        this.strategy.start();
        this.start();
    }
    
    private Collection<Message> interpretData (T t) {
        Collection<Message> messages = new ArrayList<>();
        
        // TODO: use CompletionService for async processing

        for (Interpreter<T> trans : this.interpreters) {
            Message[] translated = trans.interpret(t);
            if (translated != null && translated.length != 0) {
                messages.addAll(Arrays.asList(translated));
            }
        }
        
        return messages;
    }
    
    private void processInformation(Collection<Message> messages) {
        for (Message msg : messages) {
            Indication indication = (Indication) msg;
            if (this.strategy.getSupportedIndicators().contains(indication.getId())) {
                this.strategy.send(msg);
            } else {
                this.log.debug(this.name + " agent unable to process indication: " + indication.getId() + " using strategy " + this.strategy.getName());
            }
        }
    }
    
    private void processPostTrade() {
        Collection<TradePostMortem> tradePostMortems = new ArrayList<TradePostMortem>();
        this.broker.getFinishedTrades(tradePostMortems);
        if (!tradePostMortems.isEmpty()) {
            this.strategy.process(tradePostMortems);
        }
    }
    
    private void processTradeSignals() throws ExchangeException {
        Collection<Trade> tradeSignals = new ArrayList<Trade>();
        this.strategy.receive(tradeSignals, 50); // 50 is arbitrary
        
        if (!tradeSignals.isEmpty()) {
            for (Trade trade : tradeSignals) {
                this.broker.activate(trade);
            }
        }
    }
    
    public void setBroker(TradeBroker broker) {
        // Normally, I wouldn't mind someone resetting the broker. But, in this
        // case, we extablished a warm, fuzzy bond with it.  We *could* break
        // up with it, but we'd have to clear out the finished trades, etc.
        // So, I'm not supporting that yet - you either set the Broker in the
        // constructor - or you get to set it once here.
        if (this.broker != null) {
            throw new IllegalStateException("Broker is already set");
        }
        if (broker == null) {
            throw new IllegalArgumentException("broker cannot be null");
        }
        this.broker = broker;
    }

    public void setDataWriter(RawDataWriter<T> dataWriter) {
        this.dataWriter = dataWriter;
    }
    
    private void start() {
        if (!this.started) {
            this.runner = new AgentThread();
            this.runner.start();
            this.started = true;
        }
    }
    
    private void stop() {
        if (this.started) {
            
            try {
                this.broker.cancelAll();
            } catch (ExchangeException ee) {
                this.log.error(null, ee);
            }
            
            this.runner.running = false;
            this.runner.interrupt();
            this.runner = null;
            this.started = false;
        }
    }
    
    public void uninitialize() {
        this.stop();
        this.strategy.stop();
    }
}
