/**
 * 
 */

package com.zygon.exchange.trade.exchange.mtgox;

import com.xeiam.xchange.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.service.marketdata.polling.PollingMarketDataService;
import com.zygon.exchange.trade.FeedProvider;

/**
 *
 * @author zygon
 */
public class MtGoxTickProvider implements FeedProvider<Ticker> {
    
    private final PollingMarketDataService marketDataService;

    public MtGoxTickProvider(PollingMarketDataService marketDataService) {
        this.marketDataService = marketDataService;
    }
    
    @Override
    public Ticker get() {
        
//        com.xeiam.xchange.dto.marketdata.Trades trades = marketDataService.getTrades(Currencies.BTC, Currencies.USD);
//        for (Trade trade : trades.getTrades()) {
//            System.out.println(trade);
//        }
        
        return marketDataService.getTicker(Currencies.BTC, Currencies.USD);
    }
}
