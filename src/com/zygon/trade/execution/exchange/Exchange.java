/**
 *
 */
package com.zygon.trade.execution.exchange;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.Wallet;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.OrderBookProvider;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.TradeExecutor;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This needs a lot of work.
 * 
 * - This shouldn't have a xeiam streamingExchangeService - rather an EventProvider
 *   which supplies our own (not xeiam's) events.  We could even have an abstract
 *   xeiam/streaming exchange as a middle layer.
 *
 * - This has a single listener/handler for callbacks.. should it have specific
 *   event handlers registered on specific event codes? Probably won't need
 *   that detail for starters..
 *
 * - All of the components such as order book and account manager, etc. should
 *   probably be pushed through the constructor from the lower levels vs 
 *   abstract methods.
 *
 * - Add status for 'connected'/'disconnected'? And/or a connection manager to
 *   keep retrying if connection is lost.
 *
 * @author zygon
 */
public abstract class Exchange {

    private static final Logger log = LoggerFactory.getLogger(Exchange.class);

    private class ExchangeEventProcessor extends Thread {

        private volatile boolean running = true;

        public ExchangeEventProcessor() {
            super();
            super.setDaemon(true);
        }

        @Override
        public void run() {

            log.debug("ExchangeEventProcessor starting");

            while (this.running && Exchange.this.isConnected()) {
                try {
                    ExchangeEvent event = Exchange.this.getEvent();

                    if (event != null) {
                        log.trace("Received event " + event);
                        Exchange.this.listener.notify(event);
                    }
                    
                } catch (ExchangeException ex) {
                    if (this.running) {
                        log.error(null, ex);
                    }
                }
            }

            log.debug("ExchangeEventProcessor shutting down");
        }
    }
    
    private ExchangeEventListener listener;
    private ExchangeEventProcessor processor = null;
    private boolean started = false;

    private final AccountController accntController;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderprovider;
    private final TradeExecutor tradeExecutor;

    public Exchange(
            AccountController accntController, 
            OrderBookProvider orderBookProvider, 
            OrderProvider orderprovider, 
            TradeExecutor tradeExecutor) {
        this.accntController = accntController;
        this.orderBookProvider = orderBookProvider;
        this.orderprovider = orderprovider;
        this.tradeExecutor = tradeExecutor;
    }
    
    public void cancelOrder(String username, String orderId) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Cancel order request for order id {}", new Date(), orderId);

        this.tradeExecutor.cancel(username, orderId);
    }
    
    public final AccountController getAccountController() {
        return this.accntController;
    }

    public final OrderBookProvider getOrderBookProvider() {
        return this.orderBookProvider;
    }

    public final TradeExecutor getTradeExecutor() {
        return this.tradeExecutor;
    }

    public final OrderProvider getOrderProvider() {
        return this.orderprovider;
    }

    protected abstract ExchangeEvent getEvent() throws ExchangeException;
    
    public void getOpenOrders(List<LimitOrder> orders) {
        // could trace
        this.orderBookProvider.getOpenOrders(orders);
    }

    public void getOrderBook(String username, OrderBook book, String tradeableIdentifier, String currency) {
        // could trace
        this.orderBookProvider.getOrderBook(username, book, tradeableIdentifier, currency);
    }

    public LimitOrder generateLimitOrder(String id, Order.OrderType type,
            double tradableAmount, String tradableIdentifier, String transactionCurrency, double price) {
        return this.orderprovider.getLimitOrder(type, tradableAmount, tradableIdentifier, transactionCurrency, price);
    }

    public MarketOrder generateMarketOrder(String id, Order.OrderType type,
            double tradableAmount, String tradableIdentifier, String transactionCurrency) {
        return this.orderprovider.getMarketOrder(type, tradableAmount, tradableIdentifier, transactionCurrency);
    }

    public AccountInfo getAccountInfo(String username) throws ExchangeException {
        return this.accntController.getAccountInfo(username);
    }

    private Wallet getWallet(String username, String currency) throws ExchangeException {
        AccountInfo accountInfo = this.getAccountInfo(username);

        // check for null account info? check for user name equality?

        for (Wallet wallet : accountInfo.getWallets()) {
            if (wallet.getCurrency().equals(currency)) {
                return wallet;
            }
        }

        return null;
    }

    public double getBalance(String username, String currency) throws ExchangeException {
        Wallet wallet = this.getWallet(username, currency);

        if (wallet == null) {
            // TBD: open an account on the fly with 0 monies?

            throw new IllegalStateException("No wallet of currency " + currency + " found");
        }

        return wallet.getBalance().getAmount().doubleValue();
    }

    public abstract boolean isConnected();
    
    public String placeOrder(String username, Order order) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Place order request {}", new Date(), order);

        // For now let the order fail if there is not enough funds, etc.
        return this.tradeExecutor.execute(username, order);

        // TBD: post-trade operations?
    }

    public void setListener(ExchangeEventListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (!this.started) {
            this.processor = new ExchangeEventProcessor();
            this.processor.start();
            this.started = true;
        }
    }

    public void stop() {
        if (this.started) {
            this.processor.running = false;
            // shouldn't have to interrupt
            this.processor = null;
            this.started = false;
        }
    }
}
