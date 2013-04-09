/**
 * 
 */

package com.zygon.exchange.market.data;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public abstract class AbstractDataProvider<T> implements DataProvider<T> {

    private final String name;
    private final long cacheDuration;
    private final TimeUnit units;

    protected AbstractDataProvider(String name, long cacheDuration, TimeUnit units) {
        this.name = name;
        this.cacheDuration = cacheDuration;
        this.units = units;
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
}
