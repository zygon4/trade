/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.util.Aggregation;
import com.zygon.trade.market.model.indication.numeric.Volatility;
import com.zygon.analysis.util.MovingAverage;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerUtil;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.util.Type;

/**
 *
 * @author zygon
 */
public class VolatilityInterpreter extends TickerInterpreter {

    private final MovingAverage ma;
    
    // TBD: could have several aggregations and produce several volatilities
    public VolatilityInterpreter(Aggregation aggregation) {
        super();
        
        if (aggregation.getType() != Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.ma = new MovingAverage(aggregation.getDuration(), aggregation.getUnits());
    }
    
    @Override
    public Indication[] interpret(Ticker data) {
        this.ma.add(TickerUtil.getMidPrice(data), data.getTimestamp());
        
        double high = this.ma.getHigh();
        double low = this.ma.getLow();
        double volatility = high - low;
        
        return new Indication[] {
            new Volatility(data.getTradableIdentifier(), data.getTimestamp().getTime(), volatility)
        };
    }
}
