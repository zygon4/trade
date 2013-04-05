/**
 * 
 */

package com.zygon.exchange.trade.exchange.mtgox;

import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.dto.trade.MarketOrder;
import com.xeiam.xchange.service.marketdata.polling.PollingMarketDataService;
import com.xeiam.xchange.service.trade.polling.PollingTradeService;
import com.zygon.exchange.trade.FeedHandler;
import com.zygon.exchange.trade.FeedProvider;
import com.zygon.exchange.trade.MarketConditions;
import com.zygon.exchange.trade.cep.EventProcessor;
import com.zygon.exchange.trade.cep.esper.EsperEventProcessor;
import com.zygon.exchange.trade.cep.esper.indicator.SimpleMovingAverage;
import com.zygon.exchange.trade.exchange.AbstractExchange;

/**
 *
 * @author zygon
 * 
 * This is just implementing FeedProvider directly to avoid a thin inner class
 * wrapper until mtgox really needs a fully dedicated provider.
 */
public class MtGoxExchange extends AbstractExchange<Ticker> {
    
    private final com.xeiam.xchange.Exchange mtGoxROExchange = 
            ExchangeFactory.INSTANCE.createExchange(com.xeiam.xchange.mtgox.v1.MtGoxExchange.class.getName());
    
    private final com.xeiam.xchange.Exchange mtGoxRWExchange;
    
    // NO AUTH
    private final PollingMarketDataService marketDataService = mtGoxROExchange.getPollingMarketDataService();
    private final MtGoxTickProvider feedProvider = new MtGoxTickProvider(this.marketDataService);
    private final FeedHandler<Ticker> feedDistributor;
    
    public MtGoxExchange() {
        super("MTGOX");
        // TBD: some sort of init concept
        
        ExchangeSpecification exchangeSpecification = new ExchangeSpecification(com.xeiam.xchange.mtgox.v1.MtGoxExchange.class.getName());
        exchangeSpecification.setApiKey("150c6db9-e5ab-47ac-83d6-4440d1b9ce49");
        exchangeSpecification.setSecretKey("olHM/yl3CAuKMXFS2+xlP/MC0Hs1M9snHpaHwg0UZW52Ni0Tf4FhGFELO9cHcDNGKvFrj8CgyQUA4VsMTZ6dXg==");
        
//        exchangeSpecification.setUserName("1841d4baad91f25a662785cd004a3e09");
//        exchangeSpecification.setPassword("bfb5c399f3e4b07789d7ac1acea983ba");
        exchangeSpecification.setUri("https://mtgox.com");
        
        this.mtGoxRWExchange = ExchangeFactory.INSTANCE.createExchange(exchangeSpecification);
        
        // Soo... this creation/binding of processors is BS.. we need to be able
        // to more generically attach to processors.. the listener/processor
        // meeting point is vague at best.
        EventProcessor[] processors = { new EsperEventProcessor(null, null, new MarketConditions())};
//        this.feedDistributor = new VWAP(processors);
        this.feedDistributor = new SimpleMovingAverage(processors, 60, 5);
    }

    @Override
    public FeedHandler<Ticker> getFeedDistributor() {
        return this.feedDistributor;
    }
    
    @Override
    public FeedProvider<Ticker> getFeedProvider() {
        return this.feedProvider;
    }
    
    @Override
    public void placeMarketOrder (MarketOrder order) {
    
        // Interested in the private trading functionality (authentication)
        PollingTradeService tradeService = this.mtGoxRWExchange.getPollingTradeService();

//        String tradableIdentifier = "BTC";
//        String transactionCurrency = "USD";
//        OrderType orderType = null;
//
//        // kinda ghetto here...
//        switch (order.getType()) {
//            case ASK:
//                orderType = OrderType.ASK;
//                break;
//            case BID:
//                orderType = OrderType.BID;
//                break;
//            default:
//                throw new IllegalStateException("Unknown type");
//        }
//
//        MarketOrder marketOrder = new MarketOrder(orderType, order.getAmmount(), tradableIdentifier, transactionCurrency);

        String orderID = tradeService.placeMarketOrder(order);
        System.out.println("Market Order return value: " + orderID);
    }
}
