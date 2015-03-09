
package com.zygon.data1.provider;

import com.zygon.data1.contract.MarketDataContract;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.kraken.KrakenExchange;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import com.zygon.data1.Data;
import com.zygon.data1.data.Ticker;
import java.io.IOException;

/**
 *
 * @author zygon
 */
public class KrakenXChangeProvider extends MarketDataProvider<KrakenExchange> {

    public KrakenXChangeProvider(String name) throws Exception {
        super(name);
    }

    @Override
    protected KrakenExchange createProvider(MarketDataContract dataContract) throws IOException {
        return (KrakenExchange) ExchangeFactory.INSTANCE.createExchange(KrakenExchange.class.getName());
    }

    @Override
    protected Data get(KrakenExchange provider, MarketDataContract.MarketDataElement marketData, CurrencyPair currencyPair) throws IOException {
        Data data = null;
        
        switch (marketData) {
            case TICKER:
                
                PollingMarketDataService marketDataService = provider.getPollingMarketDataService();
                
                com.xeiam.xchange.dto.marketdata.Ticker providerTicker = marketDataService.getTicker(currencyPair);

                System.out.println(String.format("Kraken XChange: ask:[%s], bid[%s] @%s", providerTicker.getAsk(), providerTicker.getBid(), providerTicker.getTimestamp()));
        
                data = new Ticker(providerTicker);
                break;
            default:
                throw new UnsupportedOperationException(marketData.name());
        }
        
        return data;
    }
}
