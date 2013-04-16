/**
 * 
 */

package com.zygon.exchange.market.data;

import com.zygon.exchange.InformationHandler;
import com.zygon.exchange.ScheduledInformationProcessor;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author zygon
 * 
 * TODO: When using the DataManager automatically link up the providers with 
 * DataHandlers..  The user should really only need to supply the upstream 
 * DataProviders and the collection of downstream InformationHandlers.  At 
 * runtime this would find the correct DataHandlers for the DataBridge and 
 * register the InformationHandlers with DataHandlers.
 * So many handlers!
 */
public class DataManager<T_IN> {

    // TODO: make configurable
    private static final int EXEC_THREAD_POOL = 3;
    
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXEC_THREAD_POOL);
    private final ScheduledInformationProcessor processor = new ScheduledInformationProcessor(executor);
    private final DataProvider<T_IN> provider;
    private Collection<DataHandler<T_IN>> handlers;
    
    public DataManager(DataProvider<T_IN> provider, 
                       Collection<DataHandler<T_IN>> handlers) {
        this.provider = provider;
        this.handlers = handlers;
    }

    public DataManager(DataProvider<T_IN> provider) {
        this (provider, null);
    }
    
    public void initalize() {
        for (DataHandler<T_IN> handler : this.handlers) {
            handler.setService(this.executor);
            
            this.processor.register(this.provider, handler, provider.getInterval(), provider.getUnits());
        }
    }
    
    // TODO: rename
    public void setDataHandlerHandlers(Collection<InformationHandler<?>> infoHandler) {
        for (DataHandler handler : this.handlers) {
            handler.setTargets(infoHandler);
        }
    }
    
    public void setHandlers(Collection<DataHandler<T_IN>> handlers) {
        this.handlers = handlers;
    }
    
    public void unintialize() {
        this.executor.shutdown();
    }
}
