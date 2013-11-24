/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.util.Aggregation;
import com.zygon.trade.market.model.indication.numeric.RSI;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerUtil;
import com.zygon.trade.market.util.Type;
import java.util.Date;

/**
 *
 * @author zygon
 */
public class RSIInterpreter extends TickerInterpreter {

    private final MovingAverage gains;
    private final MovingAverage losses;
    
    public RSIInterpreter(Aggregation aggregation) {
        super();
        
        if (aggregation.getType() != Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.gains = new MovingAverage(aggregation.getDuration(), aggregation.getUnits());
        this.losses = new MovingAverage(aggregation.getDuration(), aggregation.getUnits());
        
        this.gains.add(0.001, new Date());
        this.losses.add(0.001, new Date());
    }

    private double lastValue = 0.0;
    
    @Override
    public Message[] interpret(Ticker data) {
        
        double price = TickerUtil.getMidPrice(data);
        boolean change = false;
        
        if (price > this.lastValue) {
            this.gains.add(price - this.lastValue, data.getTimestamp());
            change = true;
        } else if (price < this.lastValue) {
            this.losses.add(this.lastValue - price, data.getTimestamp());
            change = true;
        }
        
        if (change) {
            this.lastValue = price;

            double rs = this.gains.getMean() / this.losses.getMean();

            double rsi = 100 - (100 / (1 + rs));

            return new Message[] {
                new RSI(data.getTradableIdentifier(), data.getTimestamp().getTime(), rsi)
            };
        } else {
            return null;
        }
    }
}
