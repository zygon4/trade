
package com.zygon.data.feed.currency.mtgox;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.dto.marketdata.Ticker;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import enterprise.db.data.Context;
import enterprise.db.data.feed.currency.CurrencyEventFeed;
import java.io.IOException;

/**
 *
 * @author david.charubini
 */
public class MtGoxFeed extends CurrencyEventFeed<Ticker> {

    private final PollingMarketDataService marketDataService;
    
    public MtGoxFeed(Context ctx) {
        super(ctx, 30000);
        
        this.marketDataService = ExchangeFactory.INSTANCE.createExchange("com.xeiam.xchange.mtgox.v2.MtGoxExchange").getPollingMarketDataService();
    }

    @Override
    protected Ticker get() {
        Ticker ticker = null;
        
        try {
            ticker = this.marketDataService.getTicker(this.getTradeable(), this.getCurrency());
        } catch (ExchangeException ee) {
            ee.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        
        return ticker;
    }
}
