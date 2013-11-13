
package com.zygon.trade.trade;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.exchange.Exchange;
import com.zygon.trade.execution.exchange.ExchangeEvent;
import com.zygon.trade.execution.exchange.ExchangeEventListener;
import com.zygon.trade.strategy.TradePostMortem;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this element is to take Trades from an agent/strategy
 * and marshal them into individual execution actions.  Also, monitoring
 * orders which have stop losses associated but the underlying exchange
 * does not support them.
 *
 * @author zygon
 */
public class TradeBroker implements ExchangeEventListener {

    private final Object tradeLock = new Object();
    private final ArrayList<TradePostMortem> finishedTrades = new ArrayList<TradePostMortem>();
    private final Logger log;
    private final Map<Integer, Order> exchangeByTradeId = new HashMap<Integer, Order>();
    private final Exchange exchange;

    public TradeBroker(Exchange exchange) {
        this.log = LoggerFactory.getLogger(TradeBroker.class);
        this.exchange = exchange;
        
        this.exchange.setListener(this);
    }

    // TODO: persistant trade id - if the broker goes down and comes back up
    // with active orders then things are all fucked up.
    private static int tradeID = 0;
    
    public synchronized void activate(Trade trade) throws ExchangeException {
        this.log.trace("Activating trade: " + trade);
        
        String id = String.valueOf(tradeID);
        
        for (TradeSignal signal : trade.getTradeSignals()) {
            
            // TODO: limit order
            // TBD: priority
            
            MarketOrder order = this.exchange.generateMarketOrder(id, signal.getDecision().getType().getOrderType(), 
                             signal.getVolume(), signal.getTradeableIdentifier(), signal.getCurrency());
            
            this.exchange.placeOrder("TODO", order);
            
            this.exchangeByTradeId.put(tradeID, order);
            
            tradeID ++;
        }
    }
    
    public void cancelAll() {
        this.log.info("Cancelling all trades");
        // TODO: cancel active trades
    }

    public void getFinishedTrades(Collection<TradePostMortem> col) {
    
        if (!this.finishedTrades.isEmpty()) {
            synchronized (this.tradeLock) {
                col.addAll(this.finishedTrades);
                this.finishedTrades.clear();
            }
        }
    }
    
    @Override
    public void notify(ExchangeEvent event) {
        this.log.trace("Received event: " + event);
        
        // TBD: event should have the order's id
        
        // TODO: look into map. update ongoing trade or remove from map
        
        // TODO: trade history collection that shows all of the trades. 
        // Someone can come in and grab them.
        
        synchronized (this.tradeLock) {
        // TODO: finish
//        this.finishedTrades.add(new TradePostMortem(null, null, tradeID, tradeID));
        }
    }
}
