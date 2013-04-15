/**
 * 
 */

package com.zygon.exchange.market.data.handler;

import com.xeiam.xchange.dto.marketdata.Ticker;
import com.zygon.exchange.market.Price;
import com.zygon.exchange.market.data.DataHandler;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceHandler extends DataHandler<Ticker, Object> {
    
    public TickerPriceHandler(String name) {
        super(name);
    }
    
    @Override
    protected Price translate(Ticker in) {
        return new Price(in.getTradableIdentifier(), in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP), in.getTimestamp());
    }
}
