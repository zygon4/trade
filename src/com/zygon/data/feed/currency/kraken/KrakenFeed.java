
package com.zygon.data.feed.currency.kraken;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.zygon.data.Context;
import com.zygon.data.feed.currency.CurrencyEventFeed;

/**
 *
 * @author david.charubini
 * 
 * TODO: move to behind the trade package wall
 */
public class KrakenFeed extends CurrencyEventFeed<Ticker> {

    private final PollingMarketDataService marketDataService;
    
    public KrakenFeed(Context ctx) {
        super(ctx, 15000);
        
        this.marketDataService = ExchangeFactory.INSTANCE.createExchange("com.xeiam.xchange.kraken.KrakenExchange").getPollingMarketDataService();
    }

    @Override
    protected Ticker get() {
        Ticker ticker = null;
        
        try {
            ticker = this.marketDataService.getTicker(this.getTradeable(), this.getCurrency());
        } catch (ExchangeException ee) {
            ee.printStackTrace();
        }
        
        return ticker;
    }
}
