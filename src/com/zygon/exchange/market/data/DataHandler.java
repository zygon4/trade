/**
 * 
 */

package com.zygon.exchange.market.data;

import com.zygon.exchange.InformationBuffer;
import com.zygon.exchange.InformationHandler;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author zygon
 * 
 */
public class DataHandler<T_IN, T_OUT> extends InformationBuffer<T_IN, T_OUT> {

    public DataHandler(String name, InformationHandler<T_OUT> target) {
        super(name, Collections.unmodifiableList(Arrays.asList(target)));
    }
    
    public DataHandler(String name) {
        super(name);
    }
}
