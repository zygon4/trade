/**
 * 
 */

package com.zygon.trade.market.data;

import com.zygon.trade.InformationProvider;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public interface DataProvider<T> extends InformationProvider<T> {
    public Collection<T> getHistoric();
    public long getInterval();
    public TimeUnit getUnits();
    public boolean hasHistoricInformation();
    
    /*
     * TODO:
     * /**
     *  * Returns true if the DataProvider is required to be registered
     *  * with a scheduled executor.
     *  *
     * 
     * public boolean isPollBased()
     */
}
