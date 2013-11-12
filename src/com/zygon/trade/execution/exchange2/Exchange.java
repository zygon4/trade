/**
 *
 */
package com.zygon.trade.execution.exchange2;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.account.AccountInfo;
import com.xeiam.xchange.dto.marketdata.OrderBook;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.dto.trade.Wallet;
import com.xeiam.xchange.service.streaming.ExchangeEvent;
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
public final class Exchange {

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
                    ExchangeEvent event = this.streamingService.getNextEvent();

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
//                    case TICKER:
//                      Ticker ticker = (Ticker) exchangeEvent.getPayload();
//                      System.out.println(ticker.toString());
//                      break;

                        case TRADE:
                            Trade trade = (Trade) event.getPayload();
                            // TODO: convert to propert exchange event
                            com.zygon.trade.execution.exchange2.ExchangeEvent exchangeEvent = new com.zygon.trade.execution.exchange2.ExchangeEvent();
                            this.listener.notify(exchangeEvent);
                            break;

//                    case DEPTH:
//                      OrderBookUpdate orderBookUpdate = (OrderBookUpdate) exchangeEvent.getPayload();
//                      System.out.println(orderBookUpdate.toString());
//                      break;

                        default:
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

    /**
     * This represents everything the controller needs to manipulate an order
     * from placing it, monitoring it, canceling it, updating the proper
     * OrderBook, handling accounting, etc.
     */
    public static interface Binding {

        public AccountController getAccountController();

        public OrderBookProvider getOrderBookProvider();

        public TradeExecutor getTradeExecutor();

        public OrderProvider getOrderProvider();
    }
    private final Binding binding;
    private final StreamingExchangeService streamingService;
    private ExchangeEventListener listener;
    private ExchangeEventProcessor processor = null;
    private boolean started = false;

    public Exchange(Binding binding, StreamingExchangeService streamingService) {
        this.binding = binding;
        this.streamingService = streamingService;
    }

    public void cancelOrder(String username, String orderId) throws ExchangeException {
        // TODO: log impl with timestamps
        log.info("{} Cancel order request for order id {}", new Date(), orderId);

        this.binding.getTradeExecutor().cancel(username, orderId);
    }

    public void getOpenOrders(List<LimitOrder> orders) {
        // could trace
        this.binding.getOrderBookProvider().getOpenOrders(orders);
    }

    public void getOrderBook(String username, OrderBook book, String tradeableIdentifier, String currency) {
        // could trace
        this.binding.getOrderBookProvider().getOrderBook(username, book, tradeableIdentifier, currency);
    }

    public LimitOrder generateLimitOrder(String id, Order.OrderType type,
            double tradableAmount, String tradableIdentifier, String transactionCurrency, double price) {
        return this.binding.getOrderProvider().getLimitOrder(type, tradableAmount, tradableIdentifier, transactionCurrency, price);
    }

    public MarketOrder generateMarketOrder(String id, Order.OrderType type,
            double tradableAmount, String tradableIdentifier, String transactionCurrency) {
        return this.binding.getOrderProvider().getMarketOrder(type, tradableAmount, tradableIdentifier, transactionCurrency);
    }

    public AccountInfo getAccountInfo(String username) {
        return this.binding.getAccountController().getAccountInfo(username);
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
        return this.binding.getTradeExecutor().execute(username, order);

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
