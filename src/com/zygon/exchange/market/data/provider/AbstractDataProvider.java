/**
 * 
 */

package com.zygon.exchange.market.data.provider;

import com.zygon.exchange.market.data.DataProvider;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 * 
 */
public abstract class AbstractDataProvider<T> implements DataProvider<T> {

    private final String name;
    private final long cacheDuration;
    private final TimeUnit units;
    private DataLogger<T> logger = null;
    
    protected AbstractDataProvider (String name, long cacheDuration, TimeUnit units) {
        this.name = name;
        this.cacheDuration = cacheDuration;
        this.units = units;
    }

    @Override
    public Collection<T> getHistoric() {
        throw new UnsupportedOperationException("Not supported by this data provider.");
    }
    
    @Override
    public long getInterval() {
        return this.cacheDuration;
    }

    public String getName() {
        return this.name;
    }
    
    @Override
    public TimeUnit getUnits() {
        return this.units;
    }
    
    protected abstract T getData();

    @Override
    public T get() {
        T data = this.getData();
        
        if (this.logger != null) {
            this.logger.log(data);
        }
        
        return data;
    }

    @Override
    public boolean hasHistoricInformation() {
        return false;
    }

    public void setLogger(DataLogger<T> logger) {
        this.logger = logger;
    }
}
