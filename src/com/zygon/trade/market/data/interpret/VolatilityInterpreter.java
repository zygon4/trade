/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.numeric.Volatility;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.util.TickerUtil;

/**
 *
 * @author zygon
 */
public class VolatilityInterpreter extends TickerInterpreter {

    private final MovingAverage ma;
    
    // TBD: could have several aggregations and produce several volatilities
    public VolatilityInterpreter(Aggregation aggregation) {
        super();
        
        if (aggregation.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.ma = new MovingAverage(getWindow(aggregation));
    }
    
    @Override
    public Message[] interpret(Ticker data) {
        this.ma.add(TickerUtil.getMidPrice(data));
        
        double high = this.ma.getHigh();
        double low = this.ma.getLow();
        double volatility = high - low;
        
        return new Message[] {
            new Volatility(data.getTradableIdentifier(), data.getTimestamp(), volatility)
        };
    }
}
