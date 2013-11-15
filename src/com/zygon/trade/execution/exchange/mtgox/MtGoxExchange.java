
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
import com.zygon.trade.execution.exchange.ExchangeEvent;
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
    private boolean isConnected = false;
    
    public MtGoxExchange() {
        super (new MtGoxAcctController(new MtGoxPollingAccountService(getExchangeSpecification())),
               new MtGoxOrderBookProvider(new MtGoxPollingTradeService(getExchangeSpecification()), new MtGoxPollingMarketDataService(getExchangeSpecification())),
               new MtGoxOrderProvider(CurrencyUnit.USD),
               new MtGoxTradeExecutor(new MtGoxPollingTradeService(getExchangeSpecification())));
        
        this.service = getService();
    }
    
    @Override
    public ExchangeEvent getEvent() throws ExchangeException {
        
        ExchangeEvent.EventType eventType = null;
        
        try {
            com.xeiam.xchange.service.streaming.ExchangeEvent event = this.service.getNextEvent();

            switch (event.getEventType()) {

                case CONNECT:
                    if (!this.isConnected) {
                        log.info("Connected to exchange");
                        this.isConnected = true;
                        // TBD: kill any connection task
                    }
                    break;

                case DISCONNECT:
                    if (this.isConnected) {
                        this.isConnected = false;
                        log.info("Disconnected from exchange");
                        // TBD: connection task that keeps trying to
                        // to reconnect 
                    }

                    break;

                case ERROR:
                    log.info("Exchange error: " + event.getData());
                    break;

                case USER_ORDER:
                    eventType = ExchangeEvent.EventType.TRADE_FILL;
                    MtGoxOpenOrder order = (MtGoxOpenOrder) event.getPayload();
                    log.info("User order: " + order);
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
                        MtGoxTradeLag lag = (MtGoxTradeLag) event.getPayload();
                        //todo:
                        break;

//                    case DEPTH:
//                      OrderBookUpdate orderBookUpdate = (OrderBookUpdate) exchangeEvent.getPayload();
//                      System.out.println(orderBookUpdate.toString());
//                      break;

                default:
                    // log?
                    break;
            }

            if (eventType != null) {
                return new ExchangeEvent(eventType);
            }

        } catch (InterruptedException ie) {
            log.error(null, ie);
        }

        return null;
    }
    
    @Override
    public boolean isConnected() {
        return this.isConnected;
    }
}
