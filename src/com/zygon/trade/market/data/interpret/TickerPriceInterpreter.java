/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.xeiam.xchange.currency.Currencies;
import com.zygon.trade.market.model.indication.numeric.Price;
import com.zygon.trade.market.data.Ticker;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceInterpreter extends TickerInterpreter {

    private Price previousPrice = null;

    public TickerPriceInterpreter() {
        super();
    }
    
    @Override
    public Price[] interpret(Ticker in) {
        
        if (this.previousPrice == null) {
            
            this.previousPrice = new Price (in.getTradableIdentifier(), in.getTimestamp(), 
                in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue(), Currencies.USD);
            return new Price[] { this.previousPrice };
            
        } else {
            Price newPrice = new Price (in.getTradableIdentifier(), in.getTimestamp(), 
                in.getAsk().plus(in.getBid()).dividedBy(2, RoundingMode.UP).getAmount().doubleValue(), Currencies.USD);
            
            if (newPrice.getValue() != this.previousPrice.getValue()) {
                this.previousPrice = newPrice;
                return new Price[] { newPrice };
            }
        }
        
        return new Price[]{};
    }
}
