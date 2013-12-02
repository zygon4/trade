
package com.zygon.trade.trade;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.exchange.Exchange;
import com.zygon.trade.execution.exchange.ExchangeEvent;
import com.zygon.trade.execution.exchange.ExchangeEventListener;
import com.zygon.trade.execution.exchange.TickerEvent;
import com.zygon.trade.execution.exchange.TradeFillEvent;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
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

    // TBD: might be nice to formalize this little thing.. add setters, etc
    public static class TradeSummary {
        private int totalOrdersExecuted = 0;
        private int totalTradeCount = 0;
        private int cancelledTradeCount = 0;

        public int getCancelledTradeCount() {
            return this.cancelledTradeCount;
        }

        public int getTotalTradeCount() {
            return this.totalTradeCount;
        }
    }
    
    private final ArrayList<TradePostMortem> finishedTrades = new ArrayList<TradePostMortem>();
    private final Logger log;
    private final Map<String, TradeMonitor> tradeMonitorsByTradeId = new HashMap<String, TradeMonitor>();
    private final String accountId;
    private final Exchange exchange;
    private final TradeSummary tradeSummary = new TradeSummary();
    private final ArrayBlockingQueue<ExchangeEvent> exchangeEventQueue = new ArrayBlockingQueue<ExchangeEvent>(10000);
    private final Object lock = new Object();
    
    private Runnable eventHandler = null;
    private Ticker ticker = null;
    
    public TradeBroker(String accountId, Exchange exchange) {
        this.log = LoggerFactory.getLogger(TradeBroker.class);
        this.accountId = accountId;
        this.exchange = exchange;
        
        this.exchange.setListener(this);
    }
    
    // TODO: persistant trade id - if the broker goes down and comes back up
    // with active orders then things are all fucked up.
    private static int tradeID = 0;
    
    public void activate(Trade trade) throws ExchangeException {
        
        // TBD: for now we just want one trade at a time.. we need a better
        // set of restrictions
        if (!this.tradeMonitorsByTradeId.isEmpty()) {
            return;
        }
        
        if (this.ticker == null) {
            this.log.trace("Broker is not ready. unable to activate trade: " + trade);
        }
        
        double currentPrice = this.getCurrentPrice();

        AccountInfo accountInfo = this.exchange.getAccountController().getAccountInfo(this.accountId);

        // TBD: perform real account/risk analsysis
        for (TradeSignal signal : trade.getTradeSignals()) {

            BigMoney signalCurrencybalance = accountInfo.getBalance(CurrencyUnit.of(signal.getCurrency()));
            double desiredVolume = signal.getVolumeObjective().getVolume(signalCurrencybalance.getAmount().doubleValue(), currentPrice);

            if (desiredVolume > signalCurrencybalance.getAmount().doubleValue()) {

                this.log.trace("Unable to fund trade: " + trade);
                return;
            }
        }

        String tradeId = String.valueOf(tradeID);

        this.log.info("Activating trade: " + trade + ", id: " + tradeId);
        
        TradeMonitor monitor_v2 = new TradeMonitor(trade, accountInfo, this.exchange.getOrderProvider());
        
        Collection<Order> openingOrders = monitor_v2.open(new Signal("TODO:entry-signal"), tradeId, currentPrice);
        
        if (!openingOrders.isEmpty()) {
            this.tradeMonitorsByTradeId.put(tradeId, monitor_v2);
            for (Order order : openingOrders) {
                this.exchange.placeOrder(this.accountId, order);
                this.tradeSummary.totalOrdersExecuted ++;
            }
        }

        trade.setId(tradeID);
        tradeID ++;
    }
    
    public void cancelAll() throws ExchangeException {
        this.log.info("Cancelling all trades");
        
        synchronized (this.tradeMonitorsByTradeId) {
            Iterator<String> iter = this.tradeMonitorsByTradeId.keySet().iterator();
            while (iter.hasNext()) {
                
                String tradeId = iter.next();
                TradeMonitor monitor = this.tradeMonitorsByTradeId.get(tradeId);
                
                // These will only be for closing orders until limit orders are in place
                Collection<Order> orders = monitor.cancel();
                for (Order order : orders) {
                    this.exchange.placeOrder(this.accountId, order);
                    this.tradeSummary.totalOrdersExecuted ++;
                }
            }
        }
    }
    
    public int getActiveTradeCount() {
        return this.tradeMonitorsByTradeId.size();
    }
    
    public TradeSummary getTradeSummary() {
        return this.tradeSummary;
    }
    
    private double getCurrentPrice() {
        return TickerUtil.getMidPrice(this.ticker);
    }

    public Exchange getExchange() {
        return this.exchange;
    }
    
    public void getFinishedTrades(Collection<TradePostMortem> col) {
        synchronized (this.finishedTrades) {
            if (!this.finishedTrades.isEmpty()) {
                col.addAll(this.finishedTrades);
                this.finishedTrades.clear();
            }
        }
    }
    
    @Override
    public void notify(ExchangeEvent event) {
        this.log.trace("Received event: " + event.getDisplayString());
        
        try {
            this.exchangeEventQueue.put(event);
        } catch (InterruptedException intr) {
            this.log.debug(null, intr);
        }
        
        synchronized (this.lock) {
            if (this.eventHandler == null) {
                this.eventHandler = new Runnable() {

                    @Override
                    public void run() {
                        
                        while (!exchangeEventQueue.isEmpty()) {
                            ExchangeEvent event = exchangeEventQueue.poll();
                            if (event != null) {
                                handleEvent(event);
                            }
                        }
                        
                        synchronized (lock) {
                            eventHandler = null;
                        }
                    }
                };
                
                new Thread(this.eventHandler).start();
            }
        }
    }
    
    private void handleEvent(ExchangeEvent event) {
        switch (event.getEventType()) {
            case ACCOUNT_STATUS:
                // TODO: get account balance for volume/risk adjustment
                break;
            case TICKER:
                TickerEvent tickerEvent = (TickerEvent) event;
                this.ticker = tickerEvent.getTicker();

                // ticker signifies a price change - we might need to close
                // out some trades.
                try {
                    this.processOpenTrades();
                } catch (ExchangeException ee) {
                    this.log.error(null, ee);
                }

                break;
            case TRADE_CANCEL:
                throw new UnsupportedOperationException();
            case TRADE_FILL:
                TradeFillEvent fillEvent = (TradeFillEvent) event;
                String tradeId = TradeMonitor.getTradeId(fillEvent.getOrderID());
                String orderId = TradeMonitor.getOrderId(fillEvent.getOrderID());
                
                synchronized (this.tradeMonitorsByTradeId) {
                    TradeMonitor filledTradeMonitor = this.tradeMonitorsByTradeId.get(tradeId);
                    
                    if (filledTradeMonitor != null) {
                        filledTradeMonitor.notifyOrderFill(fillEvent.getOrderID(), fillEvent.getFillPrice(), fillEvent.getFillAmmount());
                        
                        if (filledTradeMonitor.isClosed()) {
                            this.log.info("Closing trade with id: " + tradeId);
                            
                            synchronized (this.finishedTrades) {
                                this.finishedTrades.add(filledTradeMonitor.getPostMortem());
                            }

                            this.tradeSummary.totalTradeCount ++;
                            this.tradeMonitorsByTradeId.remove(tradeId);
                        }
                    }
                }

                break;
            case TRADE_REJECTED:
                break;
        }
    }
    
    private void processOpenTrades() throws ExchangeException {
        double price = this.getCurrentPrice();
        
        synchronized (this.tradeMonitorsByTradeId) {
            Iterator<String> iter = this.tradeMonitorsByTradeId.keySet().iterator();
            while (iter.hasNext()) {
                
                String tradeId = iter.next();
                TradeMonitor monitor = this.tradeMonitorsByTradeId.get(tradeId);
                
                // These will only be for closing orders until limit orders are in place
                Collection<Order> orders = monitor.notifyPriceUpdate(price);
                for (Order order : orders) {
                    this.exchange.placeOrder(this.accountId, order);
                    this.tradeSummary.totalOrdersExecuted ++;
                }
            }
        }
    }
}
