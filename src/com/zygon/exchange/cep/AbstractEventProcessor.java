/**
 * 
 */

package com.zygon.exchange.cep;

import com.zygon.exchange.AbstractInformationHandler;
import com.zygon.exchange.trade.MarketConditions;
import com.zygon.exchange.trade.OrderProvider;
import com.zygon.exchange.trade.TradeExecutor;

/**
 *
 * @author zygon
 * 
 * I think this should be refactored to run on top of the indications.
 * 
 */
public abstract class AbstractEventProcessor<EVENT_TYPE> extends AbstractInformationHandler<EVENT_TYPE> {

    private final OrderProvider orderProvider;
    private final TradeExecutor trader;
    private final MarketConditions marketConditions;

    public AbstractEventProcessor(String name, OrderProvider orderProvider, TradeExecutor trader, MarketConditions marketConditions) {
        super(name);
        this.orderProvider = orderProvider;
        this.trader = trader;
        this.marketConditions = marketConditions;
    }

    @Override
    public void handle(EVENT_TYPE event) {
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
