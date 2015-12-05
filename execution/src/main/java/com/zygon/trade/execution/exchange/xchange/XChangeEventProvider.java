
package com.zygon.trade.execution.exchange.xchange;

import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.xeiam.xchange.service.streaming.StreamingExchangeService;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.exchange.ExchangeError;
import com.zygon.trade.execution.exchange.ExchangeEvent;
import com.zygon.trade.execution.exchange.ExchangeEventProvider;
import com.zygon.trade.execution.exchange.TickerEvent;
import com.zygon.trade.execution.exchange.TradeEvent;
import com.zygon.trade.execution.exchange.TradeFillEvent;
import com.zygon.trade.execution.exchange.TradeLagEvent;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class XChangeEventProvider implements ExchangeEventProvider {

    private static final Logger log = LoggerFactory.getLogger(XChangeEventProvider.class);

    private final StreamingExchangeService service;

    public XChangeEventProvider(StreamingExchangeService service) {
        this.service = service;
    }

    @Override
    public ExchangeEvent getEvent() throws ExchangeException {

        ExchangeEvent event = null;

        try {
            com.xeiam.xchange.service.streaming.ExchangeEvent exchangeEvent = this.service.getNextEvent();

            switch (exchangeEvent.getEventType()) {
                case MESSAGE:
                case EVENT:
                case WELCOME:
                case AUTHENTICATION:
                case SUBSCRIBE_DEPTH:
                case SUBSCRIBE_ORDERS:
                case SUBSCRIBE_TICKER:
                case PRIVATE_ID_KEY:
                case ORDER_ADDED:
                case ORDER_CANCELED:
                case USER_ORDERS_LIST:
                case ACCOUNT_INFO:
                case USER_TRADE_VOLUME:
                case USER_ORDER_ADDED:
                case USER_ORDER_CANCELED:
                case USER_ORDER_NOT_FOUND:
                case USER_WALLET:
                case USER_MARKET_ORDER_EST:
                    // unsupported so far
                    break;
                case CONNECT:
                    event = new ExchangeEvent(ExchangeEvent.EventType.CONNECTED);
                    break;
                case DISCONNECT:
                    event = new ExchangeEvent(ExchangeEvent.EventType.DISCONNECTED);
                    break;
                case ERROR:
                    log.info("Exchange error: " + exchangeEvent.getData());
                    event = new ExchangeError(exchangeEvent.getData());
                    break;

                case USER_ORDER:
                    Order order = (Order) exchangeEvent.getPayload();
//                    MtGoxOpenOrder order = (MtGoxOpenOrder) exchangeEvent.getPayload();
                    log.info("User order: " + order);
                    // TODO: this is incomplete- this will require some study.
//                    event = new TradeFillEvent(order.getId(), TradeFillEvent.Fill.FULL,
//                            order.getPrice().getValue().doubleValue(), order.getAmount().getValue().doubleValue());
                    break;
                case TICKER:
                    Ticker ticker = (Ticker) exchangeEvent.getPayload();
                    System.out.println(ticker.toString());
                    event = new TickerEvent(new com.zygon.trade.market.data.Ticker(ticker));
                    break;
                case TRADE:
                    Trade trade = (Trade) exchangeEvent.getPayload();
                    System.out.println(trade.toString());
                    // TODO: convert to propert exchange event
                    event = new TradeEvent(trade.getId());
//                    this.listener.notify(exchangeEvent);
                    break;

                case TRADE_LAG:
//                        MtGoxTradeLag lag = (MtGoxTradeLag) exchangeEvent.getPayload();
//                        event = new TradeLagEvent(lag.getAge(), TimeUnit.SECONDS);
                    break;

                case DEPTH:
//                      OrderBookUpdate orderBookUpdate = (OrderBookUpdate) exchangeEvent.getPayload();
//                      System.out.println(orderBookUpdate.toString());
                    break;

                case USER_WALLET_UPDATE:
                    //MtGoxWalletUpdate walletUpdate = (MtGoxWalletUpdate) exchangeEvent.getPayload();

                    break;
                default:
                    // log?
                    break;
            }

            if (event == null) {
                log.debug("Unhandled exchange event: " + exchangeEvent.getEventType().name());
            }

            return event;

        } catch (InterruptedException ie) {
            log.error(null, ie);
        }

        return null;
    }


}
