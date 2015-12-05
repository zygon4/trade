
package com.zygon.analysis.core;


import com.xeiam.xchange.dto.trade.LimitOrder;
import com.zygon.data1.data.OpenOrderBook;

import java.util.Collection;

/**
 *
 * @author zygon
 */
public interface OrderGenerator {
    public Collection<LimitOrder> generateOrders(OpenOrderBook book);
}
