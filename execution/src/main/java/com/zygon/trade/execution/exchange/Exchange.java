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
 * - This has a single listener/handler for callbacks.. should it have specific
 *   event handlers registered on specific event codes? Probably won't need
 *   that detail for starters..
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
            super(ExchangeEventProcessor.class.getCanonicalName());
            super.setDaemon(true);
        }

        @Override
        public void run() {

            log.debug("ExchangeEventProcessor starting");

            while (this.running) {
                try {
                    ExchangeEvent event = Exchange.this.exchangeEventProvider.getEvent();

                    if (event != null) {
                        log.trace("Received event " + event);

                        switch (event.getEventType()) {
                            case CONNECTED:
                                if (!Exchange.this.isConnected) {
                                    log.info("Connected to exchange");
                                    Exchange.this.isConnected = true;
                                    // TBD: kill any connection task
                                }
                                break;
                            case DISCONNECTED:
                                if (Exchange.this.isConnected) {
                                    Exchange.this.isConnected = false;
                                    log.info("Disconnected from exchange");
                                    // TBD: connection task that keeps trying to
                                    // to reconnect
                                }
                                break;
                        }

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
    private boolean isConnected = false;

    private final AccountController accntController;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderprovider;
    private final TradeExecutor tradeExecutor;
    private final ExchangeEventProvider exchangeEventProvider;

    public Exchange(
            AccountController accntController,
            OrderBookProvider orderBookProvider,
            OrderProvider orderprovider,
            TradeExecutor tradeExecutor,
            ExchangeEventProvider exchangeEventProvider) {
        this.accntController = accntController;
        this.orderBookProvider = orderBookProvider;
        this.orderprovider = orderprovider;
        this.tradeExecutor = tradeExecutor;
        this.exchangeEventProvider = exchangeEventProvider;
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
        return this.orderprovider.getLimitOrder(id, type, tradableAmount, tradableIdentifier, transactionCurrency, price);
    }

    public MarketOrder generateMarketOrder(String id, Order.OrderType type,
            double tradableAmount, String tradableIdentifier, String transactionCurrency) {
        return this.orderprovider.getMarketOrder(id, type, tradableAmount, tradableIdentifier, transactionCurrency);
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

        return wallet.getBalance().doubleValue();
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public String placeOrder(String accountId, Order order) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Place order request {}", new Date(), order);

        // For now let the order fail if there is not enough funds, etc.
        return this.tradeExecutor.execute(accountId, order);

        // TBD: post-trade operations?
    }

    public final void setListener(ExchangeEventListener listener) {
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
            this.processor.interrupt();
            this.processor = null;
            this.started = false;
        }
    }
}
