
package com.zygon.trade.execution.exchange.mtgox;

import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.ExchangeSpecification;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingAccountService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingMarketDataService;
import com.xeiam.xchange.mtgox.v2.service.polling.MtGoxPollingTradeService;
import com.xeiam.xchange.mtgox.v2.service.streaming.MtGoxStreamingConfiguration;
import com.xeiam.xchange.service.streaming.StreamingExchangeService;
import com.zygon.trade.execution.AccountController;
import com.zygon.trade.execution.OrderBookProvider;
import com.zygon.trade.execution.OrderProvider;
import com.zygon.trade.execution.TradeExecutor;
import com.zygon.trade.execution.exchange.Exchange;
import org.joda.money.CurrencyUnit;

/**
 *
 * @author zygon
 */
public class MtGoxExchange extends Exchange {

    private static MtGoxStreamingConfiguration getStreamingConfig() {
        MtGoxStreamingConfiguration mtGoxStreamingConfiguration = new MtGoxStreamingConfiguration(10, 10000, 60000, true, "TODO");
        return mtGoxStreamingConfiguration;
    }
    
    private final MtGoxAcctController accntController;
    private final OrderBookProvider orderBookProvider;
    private final OrderProvider orderProvider;
    private final TradeExecutor tradeExecutor;
    
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
    
    public MtGoxExchange() {
        super (getService());
        
        ExchangeSpecification spec = getExchangeSpecification();
        
        this.accntController = new MtGoxAcctController(new MtGoxPollingAccountService(spec));
        
        MtGoxPollingTradeService tradeService = new MtGoxPollingTradeService(spec);
        MtGoxPollingMarketDataService marketDataService = new MtGoxPollingMarketDataService(spec);
        
        this.orderBookProvider = new MtGoxOrderBookProvider(tradeService, marketDataService);
        this.orderProvider = new MtGoxOrderProvider(CurrencyUnit.USD);
        this.tradeExecutor = new MtGoxTradeExecutor(tradeService);
    }

    @Override
    public AccountController getAccountController() {
        return this.accntController;
    }

    @Override
    public OrderBookProvider getOrderBookProvider() {
        return this.orderBookProvider;
    }

    @Override
    public OrderProvider getOrderProvider() {
        return this.orderProvider;
    }

    @Override
    public TradeExecutor getTradeExecutor() {
        return this.tradeExecutor;
    }
}
