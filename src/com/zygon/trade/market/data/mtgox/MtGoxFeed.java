
package com.zygon.trade.market.data.mtgox;

import com.xeiam.xchange.ExchangeException;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.service.polling.PollingMarketDataService;
import com.zygon.data.Context;
import com.zygon.data.feed.currency.CurrencyEventFeed;
import com.zygon.trade.market.data.Ticker;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author david.charubini
 * 
 */
public class MtGoxFeed extends CurrencyEventFeed<Ticker> {

    private static Logger logger = LoggerFactory.getLogger(MtGoxFeed.class);
    
    private final PollingMarketDataService marketDataService;
    
    public MtGoxFeed(Context ctx) {
        super(ctx, 30000);
        
        this.marketDataService = ExchangeFactory.INSTANCE.createExchange("com.xeiam.xchange.mtgox.v2.MtGoxExchange").getPollingMarketDataService();
    }

    @Override
    protected Ticker get() {
        Ticker ticker = null;
        
        try {
            ticker = new Ticker(this.marketDataService.getTicker(this.getTradeable(), this.getCurrency()), this.getCurrency());
        } catch (IOException io) {
            logger.error(null, io);
        } catch (ExchangeException ee) {
            logger.error(null, ee);
        }
        
        return ticker;
    }
}
