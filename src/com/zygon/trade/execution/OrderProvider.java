/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;

/**
 *
 * @author zygon
 */
public interface OrderProvider {
    public MarketOrder getMarketOrder(String id, Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency);
    public LimitOrder getLimitOrder(String id, Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency, double limitPrice);
}
