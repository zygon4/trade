/**
 * 
 */

package com.zygon.exchange.execution;

/**
 *
 * Should this be a generic order type?
 * 
 * @author zygon
 */
public interface TradeExecutor {
    public void execute(com.xeiam.xchange.dto.Order order);
}