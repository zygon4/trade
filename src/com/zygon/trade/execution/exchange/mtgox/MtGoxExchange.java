
package com.zygon.trade.execution.exchange.mtgox;

import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.mtgox.v2.dto.trade.polling.MtGoxOpenOrder;
import com.xeiam.xchange.mtgox.v2.dto.trade.streaming.MtGoxTradeLag;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingAccountService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingMarketDataService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingTradeService;
import com.xeiam.xchange.mtgox.v2.service.streaming.MtGoxStreamingConfiguration;
import com.xeiam.xchange.service.streaming.StreamingExchangeService;
import com.zygon.trade.execution.ExchangeException;
import com.zygon.trade.execution.exchange.Exchange;
import com.zygon.trade.execution.exchange.ExchangeError;
import com.zygon.trade.execution.exchange.ExchangeEvent;
import com.zygon.trade.execution.exchange.TradeFillEvent;
import com.zygon.trade.execution.exchange.TradeLagEvent;
import java.util.concurrent.TimeUnit;
import org.joda.money.CurrencyUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author zygon
 */
public class MtGoxExchange extends Exchange {

    private static final Logger log = LoggerFactory.getLogger(MtGoxExchange.class);
    
    private static MtGoxStreamingConfiguration getStreamingConfig() {
        MtGoxStreamingConfiguration mtGoxStreamingConfiguration = new MtGoxStreamingConfiguration(10, 10000, 60000, true, "TODO");
        return mtGoxStreamingConfiguration;
    }
    
    private static ExchangeSpecification getExchangeSpecification() {
        ExchangeSpecification spec = new ExchangeSpecification(com.xeiam.xchange.mtgox.v2.MtGoxExchange.class.getName());
//        spec.set
        // what else?
        spec.setSslUri("https://mtgox.com");
        
        return spec;
    }
    
    private static StreamingExchangeService getService() {
        return ExchangeFactory.INSTANCE.createExchange(getExchangeSpecification()).getStreamingExchangeService(getStreamingConfig());
    }
    
    private final StreamingExchangeService service;
    
    public MtGoxExchange() {
        super (new MtGoxAcctController(new MtGoxPollingAccountService(getExchangeSpecification())),
               new MtGoxOrderBookProvider(new MtGoxPollingTradeService(getExchangeSpecification()), new MtGoxPollingMarketDataService(getExchangeSpecification())),
               new MtGoxOrderProvider(CurrencyUnit.USD),
               new MtGoxTradeExecutor(new MtGoxPollingTradeService(getExchangeSpecification())));
        
        this.service = getService();
    }
    
    @Override
    public ExchangeEvent getEvent() throws ExchangeException {
        
        ExchangeEvent event = null;
        
        try {
            com.xeiam.xchange.service.streaming.ExchangeEvent exchangeEvent = this.service.getNextEvent();

            switch (exchangeEvent.getEventType()) {
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
                    MtGoxOpenOrder order = (MtGoxOpenOrder) exchangeEvent.getPayload();
                    log.info("User order: " + order);
                    // TODO: this is incomplete- this will require some study.
                    event = new TradeFillEvent(order.getOid(), TradeFillEvent.Fill.FULL, order.getPrice().getValue().doubleValue(), order.getAmount().getValue().doubleValue());
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
                    case TRADE_LAG:
                        MtGoxTradeLag lag = (MtGoxTradeLag) exchangeEvent.getPayload();
                        event = new TradeLagEvent(lag.getAge(), TimeUnit.SECONDS);
                        break;

//                    case DEPTH:
//                      OrderBookUpdate orderBookUpdate = (OrderBookUpdate) exchangeEvent.getPayload();
//                      System.out.println(orderBookUpdate.toString());
//                      break;

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
