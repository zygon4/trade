/**
 * 
 */

package com.zygon.exchange.execution;

/**
 *
 * @author zygon
 */
public interface OrderProvider {
    // what args??
    public com.xeiam.xchange.dto.Order get(MarketConditions conditions);
}
