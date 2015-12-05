/**
 *
 */

package com.zygon.trade.execution.exchange.xchange;

import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.zygon.trade.execution.OrderProvider;
import java.math.BigDecimal;
import java.util.Date;

/**
 * TODO: move to common area?
 *
 * @author zygon
 */
public class XChangeOrderProvider implements OrderProvider {

    @Override
    public LimitOrder getLimitOrder(String id, Order.OrderType type, double tradableAmount,
            String tradableIdentifier, String transactionCurrency, double limitPrice) {
        // TBD: id and timestamp - is this execution restriction timestamp?
        return new LimitOrder(type, BigDecimal.valueOf(tradableAmount), new CurrencyPair(tradableIdentifier, transactionCurrency),
                id, new Date(), BigDecimal.valueOf(limitPrice));
    }

    @Override
    public MarketOrder getMarketOrder(String id, Order.OrderType type, double tradableAmount,
            String tradableIdentifier, String transactionCurrency) {
        return new MarketOrder(type, BigDecimal.valueOf(tradableAmount), new CurrencyPair(tradableIdentifier, transactionCurrency),
                id, new Date());
    }
}
