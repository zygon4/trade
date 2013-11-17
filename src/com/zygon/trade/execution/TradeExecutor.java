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
    public void cancel(String accountId, String orderId) throws ExchangeException;
    /**
     * Returns a orderID.
     * @param order the order to execute.
     * @return a orderID.
     * @throws ExchangeException 
     */
    public String execute(String accountId, Order order) throws ExchangeException;
}
