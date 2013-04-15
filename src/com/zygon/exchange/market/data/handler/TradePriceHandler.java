/**
 * 
 */

package com.zygon.exchange.market.data.handler;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.Price;

/**
 *
 * @author zygon
 */
public class TradePriceHandler extends DataHandler<Trade, Object> {
    
    public TradePriceHandler(String name) {
        super(name);
    }
    
    @Override
    protected Price translate(Trade in) {
        return new Price(in.getTradableIdentifier(), in.getPrice().getAmount().doubleValue(), in.getTimestamp().getTime());
    }
}
