/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import java.util.List;

/**
 *
 * @author zygon
 */
public interface OrderBookProvider {
    public void getOpenOrders (List<LimitOrder> orders);
    public void getOrderBook(String username, OrderBook orders, String tradeableIdentifer, String currency);
}
