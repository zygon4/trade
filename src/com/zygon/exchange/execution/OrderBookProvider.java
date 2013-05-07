/**
 * 
 */

package com.zygon.exchange.execution;

import com.xeiam.xchange.dto.Order;
import java.util.List;

/**
 *
 * @author zygon
 */
public interface OrderBookProvider {
    public void getOpenOrders (List<Order> orders);
}
