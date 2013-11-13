/**
 * 
 */

package com.zygon.trade.execution.exchange.mtgox;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.zygon.trade.execution.OrderProvider;
import java.math.BigDecimal;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;

/**
 * TODO: move to common area?
 *
 * @author zygon
 */
public class MtGoxOrderProvider implements OrderProvider {

    private final CurrencyUnit currency;

    public MtGoxOrderProvider(CurrencyUnit currency) {
        this.currency = currency;
    }

    @Override
    public LimitOrder getLimitOrder(Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency, double limitPrice) {
        return new LimitOrder(type, BigDecimal.valueOf(tradableAmount), tradableIdentifier, transactionCurrency, BigMoney.of(this.currency, limitPrice));
    }

    @Override
    public MarketOrder getMarketOrder(Order.OrderType type, double tradableAmount, String tradableIdentifier, String transactionCurrency) {
        return new MarketOrder(type, BigDecimal.valueOf(tradableAmount), tradableIdentifier, transactionCurrency);
    }
}
