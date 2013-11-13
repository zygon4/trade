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
import com.xeiam.xchange.service.streaming.StreamingExchangeService;
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
 * This needs a lot of work. The concept of a binding might go away. And this
 * thing needs to have an instances of an exchange.
 *
 * @author zygon
 */
public abstract class Exchange {

    private static final Logger log = LoggerFactory.getLogger(Exchange.class);

    public static class ExchangeEventProcessor extends Thread {

        private final StreamingExchangeService streamingService;
        private final ExchangeEventListener listener;
        private volatile boolean running = true;

        public ExchangeEventProcessor(StreamingExchangeService streamingService, ExchangeEventListener listener) {
            super();
            super.setDaemon(true);

            this.streamingService = streamingService;
            this.listener = listener;
        }

        @Override
        public void run() {

            log.debug("ExchangeEventProcessor starting");

            while (this.running) {
                try {
                    com.xeiam.xchange.service.streaming.ExchangeEvent event = this.streamingService.getNextEvent();

                    log.trace("Received event " + event);

                    switch (event.getEventType()) {

                        case CONNECT:
                            log.info("Connected to exchange");
                            break;

                        case DISCONNECT:
                            // TBD: connection task that keeps trying to
                            // to reconnect
                            log.info("Disconnected from exchange");
                            break;
                            
                        case ERROR:
                            log.info("Exchange error: " + event.getData());
                            break;
//                    case TICKER:
//                      Ticker ticker = (Ticker) exchangeEvent.getPayload();
//                      System.out.println(ticker.toString());
//                      break;

//                        case USER_ORDER
//                            break;
                            
//                        case TRADE:
//                            Trade trade = (Trade) event.getPayload();
//                            // TODO: convert to propert exchange event
//                            ExchangeEvent exchangeEvent = new ExchangeEvent();
//                            this.listener.notify(exchangeEvent);
//                            break;
//
//                        case TRADE_LAG:
//                            MtGoxTradeLag lag = (MtGoxTradeLag) event.getPayload();
//                            break;
                            
//                    case DEPTH:
//                      OrderBookUpdate orderBookUpdate = (OrderBookUpdate) exchangeEvent.getPayload();
//                      System.out.println(orderBookUpdate.toString());
//                      break;

                        default:
                            // log?
                            break;
                    }
                } catch (InterruptedException ex) {
                    if (this.running) {
                        log.error(null, ex);
                    }
                }
            }

            log.debug("ExchangeEventProcessor shutting down");
        }
    }
    
    private final StreamingExchangeService streamingService;
    private ExchangeEventListener listener;
    private ExchangeEventProcessor processor = null;
    private boolean started = false;

    public Exchange(StreamingExchangeService streamingService) {
        if (streamingService == null) {
            throw new IllegalArgumentException("No null arguments permitted");
        }
        this.streamingService = streamingService;
    }

    public void cancelOrder(String username, String orderId) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Cancel order request for order id {}", new Date(), orderId);

        this.getTradeExecutor().cancel(username, orderId);
    }
    
    public abstract AccountController getAccountController();

    public abstract  OrderBookProvider getOrderBookProvider();

    public abstract TradeExecutor getTradeExecutor();

    public abstract OrderProvider getOrderProvider();

    public void getOpenOrders(List<LimitOrder> orders) {
        // could trace
        this.getOrderBookProvider().getOpenOrders(orders);
    }

    public void getOrderBook(String username, OrderBook book, String tradeableIdentifier, String currency) {
        // could trace
        this.getOrderBookProvider().getOrderBook(username, book, tradeableIdentifier, currency);
    }

    public LimitOrder generateLimitOrder(String id, Order.OrderType type,
            double tradableAmount, String tradableIdentifier, String transactionCurrency, double price) {
        return this.getOrderProvider().getLimitOrder(type, tradableAmount, tradableIdentifier, transactionCurrency, price);
    }

    public MarketOrder generateMarketOrder(String id, Order.OrderType type,
            double tradableAmount, String tradableIdentifier, String transactionCurrency) {
        return this.getOrderProvider().getMarketOrder(type, tradableAmount, tradableIdentifier, transactionCurrency);
    }

    public AccountInfo getAccountInfo(String username) {
        return this.getAccountController().getAccountInfo(username);
    }

    private Wallet getWallet(String username, String currency) {
        AccountInfo accountInfo = this.getAccountInfo(username);

        // check for null account info? check for user name equality?

        for (Wallet wallet : accountInfo.getWallets()) {
            if (wallet.getCurrency().equals(currency)) {
                return wallet;
            }
        }

        return null;
    }

    public double getBalance(String username, String currency) {
        Wallet wallet = this.getWallet(username, currency);

        if (wallet == null) {
            // TBD: open an account on the fly with 0 monies?

            throw new IllegalStateException("No wallet of currency " + currency + " found");
        }

        return wallet.getBalance().getAmount().doubleValue();
    }

    public String placeOrder(String username, Order order) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Place order request {}", new Date(), order);

        // For now let the order fail if there is not enough funds, etc.
        return this.getTradeExecutor().execute(username, order);

        // TBD: post-trade operations?
    }

    public void setListener(ExchangeEventListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (!this.started) {
            this.streamingService.connect();
            this.processor = new ExchangeEventProcessor(this.streamingService, this.listener);
            this.processor.start();
            this.started = true;
        }
    }

    public void stop() {
        if (this.started) {
            this.streamingService.disconnect();
            this.processor.running = false;
            // shouldn't have to interrupt
            this.processor = null;
            this.started = false;
        }
    }
}
