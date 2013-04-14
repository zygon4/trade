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
 * 
 * This is just a stub.. TBD if it should be a template for a Ticker
 * because we know Ticker's will generally always exist or if it should just
 * not exist..
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
