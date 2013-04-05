/**
 * 
 */

package com.zygon.exchange.trade.cep.esper;

import com.espertech.esper.client.EventBean;
import com.zygon.exchange.trade.MarketConditions;
import com.zygon.exchange.trade.OrderProvider;
import com.zygon.exchange.trade.TradeExecutor;
import com.zygon.exchange.trade.cep.AbstractEventProcessor;

/**
 *
 * @author zygon
 * 
 * TBD: make abstract??
 */
public class EsperEventProcessor extends AbstractEventProcessor<EventBean> {

    public EsperEventProcessor(OrderProvider orderProvider, TradeExecutor trader, MarketConditions marketConditions) {
        super(orderProvider, trader, marketConditions);
    }

    //
    @Override
    public void process(EventBean event) {
        System.out.println("Processing event " + event.getUnderlying());
    }
}
