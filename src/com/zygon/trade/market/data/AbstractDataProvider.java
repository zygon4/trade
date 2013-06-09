/**
 * 
 */

package com.zygon.trade.market.data;

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
        return this.getData();
    }

    @Override
    public boolean hasHistoricInformation() {
        return false;
    }
}
