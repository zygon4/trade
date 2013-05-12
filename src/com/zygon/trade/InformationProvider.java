/**
 * 
 */

package com.zygon.trade;

import java.util.Collection;

/**
 *
 * @author zygon
 * 
 */
public interface InformationProvider<T> {
    public T get();
    public Collection<T> getHistoric();
    public boolean hasHistoricInformation();
}
