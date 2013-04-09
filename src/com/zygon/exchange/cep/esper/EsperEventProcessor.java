/**
 * 
 */

package com.zygon.exchange.cep.esper;

import com.espertech.esper.client.EventBean;
import com.zygon.exchange.trade.MarketConditions;
import com.zygon.exchange.trade.OrderProvider;
import com.zygon.exchange.trade.TradeExecutor;
import com.zygon.exchange.cep.AbstractEventProcessor;

/**
 *
 * @author zygon
 * 
 * TBD: make abstract??
 */
public class EsperEventProcessor extends AbstractEventProcessor<EventBean> {

    public EsperEventProcessor(String name, OrderProvider orderProvider, TradeExecutor trader, MarketConditions marketConditions) {
        super(name, orderProvider, trader, marketConditions);
    }

    //
    @Override
    public void handle(EventBean event) {
        System.out.println("Processing event " + event.getUnderlying());
    }
}
