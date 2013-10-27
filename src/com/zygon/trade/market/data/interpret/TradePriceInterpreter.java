/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.xeiam.xchange.currency.Currencies;
import com.xeiam.xchange.dto.marketdata.Trade;
import com.zygon.trade.market.data.Interpreter;
import com.zygon.trade.market.model.indication.numeric.Price;

/**
 *
 * @author zygon
 */
public class TradePriceInterpreter implements Interpreter<Trade> {
    
    @Override
    public Price[] interpret(Trade in) {
        return new Price[] {
            
            // TODO: wrap the xeiam.xchange Trade with a local Trade that
            // provides a currency
            
            new Price(in.getTradableIdentifier(), in.getTimestamp().getTime(), 
                in.getPrice().getAmount().doubleValue(), Currencies.USD)
        };
    }
}
