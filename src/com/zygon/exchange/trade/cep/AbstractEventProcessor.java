/**
 * 
 */

package com.zygon.exchange.trade.cep;

import com.zygon.exchange.trade.MarketConditions;
import com.zygon.exchange.trade.OrderProvider;
import com.zygon.exchange.trade.TradeExecutor;

/**
 *
 * @author zygon
 */
public abstract class AbstractEventProcessor<EVENT_TYPE> implements EventProcessor<EVENT_TYPE> {

    private final OrderProvider orderProvider;
    private final TradeExecutor trader;
    private final MarketConditions marketConditions;

    public AbstractEventProcessor(OrderProvider orderProvider, TradeExecutor trader, MarketConditions marketConditions) {
        this.orderProvider = orderProvider;
        this.trader = trader;
        this.marketConditions = marketConditions;
    }

    @Override
    public void process(EVENT_TYPE event) {
        System.out.println("Processing event " + event);
        com.xeiam.xchange.dto.Order order = this.orderProvider.get(this.marketConditions);
        if (order != null) {
            System.out.println("Executing order " + order);
            this.trader.execute(order);
        } else {
            System.out.println("No order generated");
        }
    }
}
