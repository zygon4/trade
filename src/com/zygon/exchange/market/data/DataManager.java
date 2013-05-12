/**
 * 
 */

package com.zygon.exchange.market.data;

import com.zygon.exchange.InformationHandler;
import com.zygon.exchange.ScheduledInformationProcessor;
import com.zygon.exchange.market.Message;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 * 
 */
public class DataManager<T_IN> implements InformationHandler<T_IN> {

    // TODO: make configurable
    private static final int EXEC_THREAD_POOL = 3;
    
    private final Logger log;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXEC_THREAD_POOL);
    private final ScheduledInformationProcessor processor = new ScheduledInformationProcessor(executor);
    private final DataProvider<T_IN> provider;
    
    private InformationHandler<Message> handler;
    
    private Collection<DataProcessor<T_IN>> dataProcessors;
    
    public DataManager(String name,
                       DataProvider<T_IN> provider, 
                       Collection<DataProcessor<T_IN>> dataProcessors,
                       InformationHandler<Message> handler) {
        this.log = LoggerFactory.getLogger(name);
        this.provider = provider;
        this.dataProcessors = dataProcessors;
        this.handler = handler;
    }
    
    public DataManager(String name,
                       DataProvider<T_IN> provider, 
                       Collection<DataProcessor<T_IN>> dataProcessors) {
        this(name, provider, dataProcessors, null);
    }
    
    @Override
    public void handle(T_IN t) {
        
        List<Message> messages = new ArrayList<Message>();
        
        for (DataProcessor<T_IN> proc : this.dataProcessors) {
            messages.addAll(proc.process(t));
        }
        
        for (Message msg : messages) {
            if (this.handler != null) {
                this.handler.handle(msg);
            } else {
                this.log.warn("No handler available to process message " + msg);
            }
        }
    }

    @Override
    public void setHandler(InformationHandler<T_IN> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void setInfoHandler(InformationHandler<Message> handler) {
        this.handler = handler;
    }
    
    public void initalize() {
        this.processor.register(this.provider, this, this.provider.getInterval(), this.provider.getUnits());
    }
    
    public void unintialize() {
        this.executor.shutdown();
    }
}
