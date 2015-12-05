/**
 *
 */

package com.zygon.trade.trade;

import com.xeiam.xchange.dto.Order;

/**
 *
 * @author zygon
 */
public enum TradeType {
    SHORT (Order.OrderType.ASK, Order.OrderType.BID),
    LONG (Order.OrderType.BID, Order.OrderType.ASK);

    private final Order.OrderType orderType;
    private final Order.OrderType counterOrderType;

    private TradeType(Order.OrderType orderType, Order.OrderType counterOrderType) {
        this.orderType = orderType;
        this.counterOrderType = counterOrderType;
    }

    public Order.OrderType getCounterOrderType() {
        return this.counterOrderType;
    }

    public Order.OrderType getOrderType() {
        return this.orderType;
    }
}
