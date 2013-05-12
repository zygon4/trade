/**
 * 
 */

package com.zygon.trade.execution;

/**
 *
 * @author zygon
 */
public interface OrderProvider {
    // what args??
    public com.xeiam.xchange.dto.Order get(MarketConditions conditions);
}
