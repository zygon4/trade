/**
 * 
 */

package com.zygon.exchange.market.data.provider;

import com.xeiam.xchange.Currencies;
import com.xeiam.xchange.dto.marketdata.Ticker;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class TickerProvider extends AbstractDataProvider<Ticker> {

    public TickerProvider() {
        super("Ticker Provider", 15, TimeUnit.SECONDS);
    }

    @Override
    public Ticker get() {
        return Ticker.TickerBuilder.newInstance().withTradableIdentifier(Currencies.BTC).build();
    }    
}
