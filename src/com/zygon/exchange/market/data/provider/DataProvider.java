/**
 * 
 */

package com.zygon.exchange.market.data.provider;

import com.zygon.exchange.InformationProvider;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public interface DataProvider<T> extends InformationProvider<T> {
    public long getInterval();
    public TimeUnit getUnits();
}
