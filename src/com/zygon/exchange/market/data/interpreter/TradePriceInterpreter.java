/**
 * 
 */

package com.zygon.exchange.market.data.interpreter;

import com.zygon.exchange.market.data.DataHandler;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.exchange.market.model.indication.technical.Numeric;
import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class TradePriceInterpreter implements DataHandler.Interpreter<Trade> {
    
    @Override
    public Numeric interpret(Trade in) {
        return new Numeric(in.getTradableIdentifier(), Classification.PRICE.getId(), 
                in.getTimestamp().getTime(), in.getPrice().getAmount().doubleValue());
    }
}
