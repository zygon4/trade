
package com.zygon.trade.agent;

import com.zygon.data.EventFeed;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.exchange.Exchange;
import com.zygon.trade.market.Message;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.trade.TradePostMortem;
import com.zygon.trade.trade.Trade;
import com.zygon.trade.trade.TradeBroker;
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
 */
public class Agent<T> implements EventFeed.Handler<T> {

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
    private final TradeBroker broker;
    private final String name;
    private final Logger log;
    private final Collection<Interpreter<T>> interpreters;
    private final Strategy strategy;
    private final Exchange exchange;
    
    private AgentThread runner = null;
    private boolean started = false;
    
    public Agent(String name, Collection<Interpreter<T>> interpreters, Strategy strategy, Exchange exchange) {
        
        if (name == null || interpreters == null || strategy == null || exchange == null) {
            throw new IllegalArgumentException("No null arguments permitted");
        }
        
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.interpreters = interpreters;
        this.strategy = strategy;
        this.exchange = exchange;
        this.broker = new TradeBroker(this.exchange);
    }
    
    @Override
    public void handle(T t) {
        try {
            this.dataQueue.put(t);
        } catch (InterruptedException ie) {
            this.log.error(null, ie);
        }
    }
    
    public void initialize() {
        this.exchange.start();
        this.strategy.start();
        this.start();
    }
    
    private Collection<Message> interpretData (T t) {
        Collection<Message> messages = new ArrayList<>();
        
        // TODO: use CompletionService for async processing

        for (Interpreter<T> trans : this.interpreters) {
            Message[] translated = trans.interpret(t);
            if (translated != null) {
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
    
    public void start() {
        if (!this.started) {
            this.runner = new AgentThread();
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
    
    public void uninitialize() {
        this.stop();
        this.strategy.stop();
        this.exchange.stop();
    }
}
