/**
 * 
 */

package com.zygon.exchange.market.data.provider;

import com.xeiam.xchange.Currencies;
import com.xeiam.xchange.dto.Order;
import com.xeiam.xchange.dto.marketdata.Trade;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class TradeProvider extends AbstractDataProvider<Trade>{

    public TradeProvider() {
        super("Trade Provider", 60, TimeUnit.SECONDS);
    }
    
    @Override
    public Trade get() {
        return new Trade(Order.OrderType.BID, BigDecimal.ZERO, Currencies.BTC, Currencies.USD, null, new Date(System.currentTimeMillis()));
    }
}
