/**
 * 
 */

package com.zygon.exchange.market.data.interpreter;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.Price;

/**
 *
 * @author zygon
 */
public class TradePriceInterpreter implements DataHandler.Interpreter<Trade> {
    
    @Override
    public Price interpret(Trade in) {
        return new Price(in.getTradableIdentifier(), in.getPrice().getAmount().doubleValue(), in.getTimestamp().getTime());
    }
}
