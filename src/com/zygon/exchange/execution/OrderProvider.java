/**
 * 
 */

package com.zygon.exchange.execution;

import com.zygon.exchange.trade.MarketConditions;

/**
 *
 * @author zygon
 */
public interface OrderProvider {
    // what args??
    public com.xeiam.xchange.dto.Order get(MarketConditions conditions);
}
