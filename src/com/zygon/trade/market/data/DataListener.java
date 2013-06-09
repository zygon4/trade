/**
 * 
 */

package com.zygon.trade.market.data;

import com.zygon.trade.InformationHandler;
import com.zygon.trade.ScheduledInformationProcessor;
import com.zygon.trade.market.Message;
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
public class DataListener<T_IN> implements InformationHandler<T_IN> {

    // TODO: make configurable
    private static final int EXEC_THREAD_POOL = 1; //Runtime.getRuntime().availableProcessors() - 1;
    
    private final Logger log;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXEC_THREAD_POOL);
    private final ScheduledInformationProcessor processor = new ScheduledInformationProcessor(executor);
    private final DataProvider<T_IN> dataProvider;
    private final DataLogger<T_IN> dataLogger;
    
    private InformationHandler<Message> handler;
    private Collection<DataProcessor<T_IN>> dataProcessors;
    
    public DataListener(String name,
                       DataProvider<T_IN> provider, 
                       DataLogger<T_IN> dataLogger,
                       Collection<DataProcessor<T_IN>> dataProcessors,
                       InformationHandler<Message> handler) {
        this.log = LoggerFactory.getLogger(name);
        this.dataProvider = provider;
        this.dataLogger = dataLogger;
        this.dataProcessors = dataProcessors;
        this.handler = handler;
    }
    
    public DataListener(String name,
                       DataProvider<T_IN> dataProvider, 
                       DataLogger<T_IN> dataLogger,
                       Collection<DataProcessor<T_IN>> dataProcessors) {
        this(name, dataProvider, dataLogger, dataProcessors, null);
    }

    public DataLogger<T_IN> getDataLogger() {
        return this.dataLogger;
    }
    
    @Override
    public void handle(T_IN t) {
        if (this.dataLogger != null) {
            this.dataLogger.log(t);
        }
        
        List<Message> messages = new ArrayList<>();
        
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

    public void setInfoHandler(InformationHandler<Message> handler) {
        this.handler = handler;
    }

    public void initalize() {
        this.processor.register(this.dataProvider, this, this.dataProvider.getInterval(), this.dataProvider.getUnits());
    }
    
    public void unintialize() {
        this.executor.shutdown();
    }
}
