
package com.zygon.trade.trade;

import com.xeiam.xchange.dto.trade.MarketOrder;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.exchange.Exchange;
import com.zygon.trade.execution.exchange.ExchangeEvent;
import com.zygon.trade.execution.exchange.ExchangeEventListener;
import com.zygon.trade.execution.exchange.TickerEvent;
import com.zygon.trade.execution.exchange.TradeCancelEvent;
import com.zygon.trade.execution.exchange.TradeFillEvent;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.util.TickerUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The purpose of this element is to take Trades from an agent/strategy
 * and marshal them into individual execution actions.  Also, monitoring
 * orders which have stop losses associated but the underlying exchange
 * does not support them.
 * 
 * TODO: volume strategy
 *
 * @author zygon
 */
public class TradeBroker implements ExchangeEventListener {

    // TBD: might be nice to formalize this little thing.. add setters, etc
    public static class TradeSummary {
        private int totalTradeCount = 0;
        private int cancelledTradeCount = 0;

        public int getCancelledTradeCount() {
            return this.cancelledTradeCount;
        }

        public int getTotalTradeCount() {
            return this.totalTradeCount;
        }
    }
    
    // A nasty coarse grain lock
    private final Object tradeLock = new Object();
    private final ArrayList<TradePostMortem> finishedTrades = new ArrayList<TradePostMortem>();
    private final Logger log;
    private final Map<String, TradeMonitor> tradeSignalByOrderId = new HashMap<String, TradeMonitor>();
    private final String accountId;
    private final Exchange exchange;
    private final TradeSummary tradeSummary = new TradeSummary();
    
    private Ticker ticker = null;
    
    public TradeBroker(String accountId, Exchange exchange) {
        this.log = LoggerFactory.getLogger(TradeBroker.class);
        this.accountId = accountId;
        this.exchange = exchange;
        
        this.exchange.setListener(this);
    }

    // TODO: persistant trade id - if the broker goes down and comes back up
    // with active orders then things are all fucked up.
    private static int orderID = 0;
    private static int tradeID = 0;
    
    public synchronized void activate(Trade trade) throws ExchangeException {
        
        // TBD: for now we just want one trade at a time.. we need a better
        // set of restrictions
        if (!this.tradeSignalByOrderId.isEmpty()) {
            return;
        }
        
        if (this.ticker == null) {
            this.log.trace("Broker is not ready. unable to activate trade: " + trade);
        }
        
        this.log.trace("Activating trade: " + trade);
        
        String id = String.valueOf(orderID);
        
        for (TradeSignal signal : trade.getTradeSignals()) {
            
            // TODO: limit order
            // TBD: priority
            
            signal.getObjective().setPrice(this.getCurrentPrice());
            
            MarketOrder order = this.exchange.generateMarketOrder(id, signal.getDecision().getType().getOrderType(), 
                             signal.getVolume(), signal.getTradeableIdentifier(), signal.getCurrency());
            
            this.exchange.placeOrder(this.accountId, order);
            
            TradeMonitor monitor = new TradeMonitor();
            monitor.setStart(System.currentTimeMillis());
            monitor.setSignal(signal);
            monitor.setTradeId(id);
            monitor.setEnterPrice(this.getCurrentPrice());
            
            synchronized (this.tradeLock) {
                this.tradeSignalByOrderId.put(id, monitor);
            }
            
            orderID ++;
        }
        
        trade.setId(tradeID++);
    }
    
    private double calculateClosingProfit (TradeType type, double entryPrice, double currentPrice, double volume) {
        
        double priceMargin = 0.0;
        
        switch (type) {
            case LONG:
                priceMargin = currentPrice - entryPrice;
                break;
            case SHORT:
                priceMargin = entryPrice - currentPrice;
                break;
        }
        
        double profit = priceMargin * volume;
        return profit;
    }
    
    public void cancelAll() {
        this.log.info("Cancelling all trades");
        // TODO: cancel active trades
    }
    
    public int getActiveTradeCount() {
        return this.tradeSignalByOrderId.size();
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
        
        synchronized (this.tradeLock) {
            
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
                    TradeCancelEvent cancelEvent = (TradeCancelEvent) event;
                    TradeMonitor cancelledTradeMonitor = this.tradeSignalByOrderId.get(cancelEvent.getOrderId());
                    if (cancelledTradeMonitor != null) {
                        this.tradeSignalByOrderId.remove(cancelEvent.getOrderId());
                        
                        cancelledTradeMonitor.setEnd(System.currentTimeMillis());
                        
                        this.finishedTrades.add(new TradePostMortem(
                                new Signal(cancelledTradeMonitor.getSignal().getReason()), 
                                new Signal(cancelEvent.getReason()), 
                                cancelledTradeMonitor.getDuration(), 
                                0.0));
                        this.tradeSummary.cancelledTradeCount ++;
                        this.tradeSummary.totalTradeCount ++;
                    }
                    break;
                case TRADE_FILL:
                    TradeFillEvent fillEvent = (TradeFillEvent) event;
                    TradeMonitor filledTradeMonitor = this.tradeSignalByOrderId.get(fillEvent.getTradeID());
                    if (filledTradeMonitor != null) {
                        if (fillEvent.getFill() == TradeFillEvent.Fill.FULL) {
                            this.tradeSignalByOrderId.remove(fillEvent.getTradeID());
                            
                            filledTradeMonitor.setEnd(System.currentTimeMillis());
                            
                            double profit = this.calculateClosingProfit(
                                    filledTradeMonitor.getSignal().getTradeType(), 
                                    filledTradeMonitor.getEnterPrice(), 
                                    this.getCurrentPrice(), 
                                    filledTradeMonitor.getSignal().getVolume());
                            
                            this.finishedTrades.add(new TradePostMortem(
                                    new Signal(filledTradeMonitor.getSignal().getReason()), 
                                    new Signal("FILLED"), 
                                    filledTradeMonitor.getDuration(), 
                                    profit));
                            
                            this.tradeSummary.totalTradeCount ++;
                        } else {
                            // TBD: partial fill - might need to hold onto some
                            // additional meta data during a trade. and update
                            // with fill ammounts.
                        }
                    }
                    
                    break;
                case TRADE_REJECTED:
                    break;
            }
        }
    }
    
    private void processOpenTrades() throws ExchangeException {
        double price = this.getCurrentPrice();
        
        synchronized (this.tradeLock) {
            Iterator<String> iter = this.tradeSignalByOrderId.keySet().iterator();
            while (iter.hasNext()) {
                String tradeKey = iter.next();
                TradeMonitor trade = this.tradeSignalByOrderId.get(tradeKey);
                
                PriceObjective priceObjective = trade.getSignal().getObjective();
                Signal exitSignal = this.getExitSignal(trade, price, priceObjective);
                if (exitSignal != null) {
                    this.log.info("Closing trade " + trade.getTradeId() + " due to " + exitSignal.getName());
                    // We met the exit conditions - close the trade
                    // TBD: limit order
                    
                    MarketOrder closeOrder = this.exchange.generateMarketOrder(trade.getTradeId(), trade.getSignal().getTradeType().getCounterOrderType(), 
                             trade.getSignal().getVolume(), trade.getSignal().getTradeableIdentifier(), trade.getSignal().getCurrency());
            
                    this.exchange.placeOrder(this.accountId, closeOrder);
                    
                    iter.remove();
                }
            }
        }
    }
    
    private static final String EXIT_STOP_LOSS = "STOP_LOSS";
    private static final String EXIT_TAKE_PROFIT = "TAKE_PROFIT";
    
    /**
     * Returns a signal symbolizing that the trade should be closed, null otherwise.
     * @param monitor
     * @param currentPrice
     * @param priceObjective
     * @return a signal symbolizing that the trade should be closed, null otherwise.
     */
    private Signal getExitSignal(TradeMonitor monitor, double currentPrice, PriceObjective priceObjective) {
        
        String exitSignal = null;
        
        switch (monitor.getSignal().getTradeType()) {
            case LONG:
                if (currentPrice <= priceObjective.getStopLoss()) {
                    exitSignal = EXIT_STOP_LOSS;
                } else if(currentPrice >= priceObjective.getTakeProfit()) {
                    exitSignal = EXIT_TAKE_PROFIT;
                }
                break;
            case SHORT:
                if (currentPrice >= priceObjective.getStopLoss()) {
                    exitSignal = EXIT_STOP_LOSS;
                } else if (currentPrice <= priceObjective.getTakeProfit()) {
                    exitSignal = EXIT_TAKE_PROFIT;
                }
                break;
        }
        
        return exitSignal != null ? new Signal(exitSignal) : null;
    }
}
