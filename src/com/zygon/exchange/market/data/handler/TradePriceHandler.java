/**
 * 
 */

package com.zygon.exchange.market.data.handler;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.InformationHandler;
import com.zygon.exchange.market.Price;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class TradePriceHandler extends DataHandler<Trade, Object> {
    
    public TradePriceHandler(String name, Collection<InformationHandler<Object>> targets) {
        super(name, targets);
    }
    
    @Override
    protected Price translate(Trade in) {
        return new Price(in.getTradableIdentifier(), in.getPrice().getAmount().doubleValue(), in.getTimestamp().getTime());
    }
}
