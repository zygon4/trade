
package com.zygon.trade.agent;

import com.google.common.collect.Lists;
import com.zygon.data.Handler;
import com.zygon.data.RawDataWriter;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.trade.TradePostMortem;
import com.zygon.trade.trade.Trade;
import com.zygon.trade.trade.TradeBroker;
import com.zygon.trade.trade.TradeGenerator;
import com.zygon.trade.trade.TradeSummary;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
                if (this.running) {
                    Agent.this.processPostTrade();
                    try { Thread.sleep (3000); } catch (InterruptedException ignore) {}
                }
            }
        }
    }
    
    private final Logger log;
    private final String name;
    private final Collection<Interpreter<T>> interpreters;
    private final Collection<Identifier> supportedIndicators;
    private final TradeGenerator tradeGenerator;
    private final TradeSummary tradeSummary;
    
    private TradeBroker broker;
    private AgentThread runner = null;
    private boolean started = false;
    // This will be moving soon but it was insane to be here
    private RawDataWriter<T> dataWriter = null;

    public Agent(String name, 
                 Collection<Interpreter<T>> interpreters, 
                 Collection<Identifier> supportedIndicators, 
                 TradeGenerator tradeGenerator, 
                 TradeBroker broker) {
        
        if (name == null || interpreters == null || supportedIndicators == null || 
            tradeGenerator == null) {
            throw new IllegalArgumentException("No null arguments permitted");
        }
        
        this.name = name;
        this.log = LoggerFactory.getLogger(this.name);
        this.interpreters = interpreters;
        this.supportedIndicators = supportedIndicators;
        this.tradeGenerator = tradeGenerator;
        this.tradeSummary = new TradeSummary(this.name);
        this.broker = broker;
    }
    
    public TradeSummary getStrategySummary() {
        return this.tradeSummary;
    }
    
    @Override
    public void handle(T date) {
        if (this.started) {
            if (date != null) {

                // Log the data
                if (Agent.this.dataWriter != null) {
                    try {
                        Agent.this.dataWriter.log(date);
                    } catch (IOException io) {
                        if (this.started) {
                            // TBD: anything else we can do? We'd probably like
                            // to alarm/alert here.
                            Agent.this.log.error(null, io);
                        }
                    }
                }

                try {
                    // 1) interpret data
                    Collection<Indication> messages = Agent.this.interpretData(date);

                    // 2) process information
                    if (!messages.isEmpty()) {
                        Agent.this.processInformation(messages);
                    }
                } catch (ExchangeException ee) {
                    if (this.started) {
                        // TBD: anything else we can do? We'd probably like
                        // to alarm/alert here.
                        Agent.this.log.error(null, ee);
                    }
                } catch (ExecutionException exece) {
                    if (this.started) {
                        // TBD: anything else we can do? We'd probably like
                        // to alarm/alert here.
                        Agent.this.log.error(null, exece);
                    }
                } catch (InterruptedException ie) {
                    if (this.started) {
                        Agent.this.log.error(null, ie);
                    }
                }

            } else {
                this.log.debug("Received null data");
            }
        }
    }
    
    public void initialize() {
        this.start();
    }
    
    private ExecutorService newFixedThreadPool = null;
    private CompletionService<Indication[]> completionService = null;
    
    private Collection<Indication> interpretData (final T t) throws InterruptedException, ExecutionException {
        Collection<Indication> messages = Lists.newArrayList();
        
        int synchronousActions = 0;
        
        for (final Interpreter<T> trans : this.interpreters) {
            
            completionService.submit(new Callable<Indication[]>() {

                @Override
                public Indication[] call() throws Exception {
                    return trans.interpret(t);
                }
            });
            
            synchronousActions ++;
        }
        
        for (int i = 0; i < synchronousActions; i++) {
            Indication[] interpretResult = completionService.take().get();
            if (interpretResult != null && interpretResult.length != 0) {
                messages.addAll(Arrays.asList(interpretResult));
            }
         }
        
        return messages;
    }
    
    private void processInformation(Collection<Indication> messages) throws ExchangeException {
        for (Indication msg : messages) {
            
            if (this.supportedIndicators.contains(msg.getId())) {
                
                this.tradeGenerator.notify(msg);
                
                Collection<Trade> trades = this.tradeGenerator.getTrades();
                
                if (!trades.isEmpty()) {
                    for (Trade trade : trades) {
                        this.broker.activate(trade);
                    }
                }
                
            } else {
                this.log.debug(this.name + " agent unable to process indication: " + msg.getId());
            }
        }
    }
    
    private void processPostTrade() {
        Collection<TradePostMortem> tradePostMortems = Lists.newArrayList();
        
        this.broker.getFinishedTrades(tradePostMortems);
        
        for (TradePostMortem tpm : tradePostMortems) {
            this.tradeSummary.add(tpm.getProfit());
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
            this.newFixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);
            this.completionService = new ExecutorCompletionService<Indication[]>(this.newFixedThreadPool);
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
            this.newFixedThreadPool.shutdown();
            this.newFixedThreadPool = null;
            this.completionService = null;
            this.started = false;
        }
    }
    
    public void uninitialize() {
        this.stop();
    }
}
