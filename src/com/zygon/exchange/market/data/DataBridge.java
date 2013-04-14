/**
 * 
 */

package com.zygon.exchange.market.data;

import com.zygon.exchange.ScheduledInformationProcessor;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class DataBridge<T_IN> {

    private final DataProvider<T_IN> provider;
    private Collection<DataHandler<T_IN, ?>> handlers;

    public DataBridge(DataProvider<T_IN> provider, Collection<DataHandler<T_IN, ?>> handlers) {
        this.provider = provider;
        this.handlers = handlers;
    }

    public DataBridge(DataProvider<T_IN> provider) {
        this (provider, null);
    }
    
    public void initalize() {
        for (DataHandler<T_IN, ?> handler : this.handlers) {
            ScheduledInformationProcessor.instance().register(this.provider, handler, provider.getInterval(), provider.getUnits());
        }
    }
    
    public void setHandlers(Collection<DataHandler<T_IN, ?>> handlers) {
        this.handlers = handlers;
    }
    
    // no unint for now..
}
