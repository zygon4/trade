/**
 * 
 */

package com.zygon.exchange.market.data;

import com.zygon.exchange.InformationBuffer;
import com.zygon.exchange.InformationHandler;
import java.util.Collection;

/**
 *
 * @author zygon
 * 
 */
public class DataHandler<T_IN, T_OUT> extends InformationBuffer<T_IN, T_OUT> {

    public DataHandler(String name, Collection<InformationHandler<T_OUT>> targets) {
        super(name, targets);
    }
}
