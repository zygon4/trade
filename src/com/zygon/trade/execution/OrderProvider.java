/**
 * 
 */

package com.zygon.trade.execution;

import com.xeiam.xchange.dto.Order;
import java.math.BigDecimal;

/**
 *
 * @author zygon
 */
public interface OrderProvider {
    public com.xeiam.xchange.dto.Order get(Order.OrderType type, BigDecimal tradableAmount, String tradableIdentifier, String transactionCurrency);
}
