/**
 * 
 */

package com.zygon.trade.execution;

/**
 *
 * Should this be a generic order type?
 * 
 * @author zygon
 */
public interface TradeExecutor {
    public void cancel(String orderId) throws ExchangeException;
    public void execute(com.xeiam.xchange.dto.Order order) throws ExchangeException;
}
