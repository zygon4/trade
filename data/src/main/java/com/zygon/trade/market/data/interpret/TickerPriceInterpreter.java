/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.model.indication.numeric.Price;
import com.zygon.trade.market.data.Ticker;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author zygon
 */
public class TickerPriceInterpreter extends TickerInterpreter {

    private final boolean cache;
    private Price previousPrice = null;

    public TickerPriceInterpreter(boolean cache) {
        super();
        this.cache = cache;
    }
    
    public TickerPriceInterpreter() {
        this (false);
    }
    
    @Override
    public Price[] interpret(Ticker in) {
        
        if (!this.cache) {
            return new Price[] {
                new Price (in.getTradableIdentifier(), in.getTimestamp().getTime(), 
                    in.getAsk().add(in.getBid()).divide(BigDecimal.valueOf(2), RoundingMode.UP).doubleValue(), in.getCurrency()) 
            };
        } else {
            if (this.previousPrice == null) {

                this.previousPrice = new Price (in.getTradableIdentifier(), in.getTimestamp().getTime(), 
                    in.getAsk().add(in.getBid()).divide(BigDecimal.valueOf(2), RoundingMode.UP).doubleValue(), in.getCurrency());
                return new Price[] { this.previousPrice };

            } else {
                Price newPrice = new Price (in.getTradableIdentifier(), in.getTimestamp().getTime(), 
                    in.getAsk().add(in.getBid()).divide(BigDecimal.valueOf(2), RoundingMode.UP).doubleValue(), in.getCurrency());

                if (newPrice.getValue() != this.previousPrice.getValue()) {
                    this.previousPrice = newPrice;
                    return new Price[] { newPrice };
                }
            }

            return new Price[]{};
        }
    }
}
