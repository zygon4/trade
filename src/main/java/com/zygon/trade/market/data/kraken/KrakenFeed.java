
package com.zygon.trade.market.data.kraken;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.zygon.data.Context;
import com.zygon.data.feed.TradeableEventFeed;
import com.zygon.trade.market.data.Ticker;
import java.io.IOException;

/**
 *
 * @author david.charubini
 * 
 */
public class KrakenFeed extends TradeableEventFeed<Ticker> {

    private final PollingMarketDataService marketDataService;
    
    public KrakenFeed(Context ctx) {
        super(ctx, 15000);
        
        this.marketDataService = ExchangeFactory.INSTANCE.createExchange("com.xeiam.xchange.kraken.KrakenExchange").getPollingMarketDataService();
    }

    @Override
    protected Ticker get() {
        Ticker ticker = null;
        
        try {
            ticker = new Ticker(this.marketDataService.getTicker(this.getTradeable(), this.getCurrency()), this.getCurrency());
        } catch (IOException io) {
            io.printStackTrace();
        } catch (ExchangeException ee) {
            ee.printStackTrace();
        }
        
        return ticker;
    }
}
