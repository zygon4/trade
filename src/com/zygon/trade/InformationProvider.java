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
    
    //TBD: move these to the DataProvider?
    public Collection<T> getHistoric();
    public boolean hasHistoricInformation();
}
