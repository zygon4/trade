/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.Order;

/**
 *
 * @author zygon
 */
public interface OrderProvider {
    // TODO: limit order
    
    // TODO: rename to "getMarketOrder"
    public com.xeiam.xchange.dto.Order get(Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency);
}
