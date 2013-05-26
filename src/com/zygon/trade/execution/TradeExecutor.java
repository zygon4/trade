/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.Order;

/**
 * 
 * @author zygon
 */
public interface TradeExecutor {
    public void cancel(String orderId) throws ExchangeException;
    /**
     * Returns a orderID.
     * @param order the order to execute.
     * @return a orderID.
     * @throws ExchangeException 
     */
    public String execute(Order order) throws ExchangeException;
}
