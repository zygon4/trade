/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.numeric.RSI;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.util.TickerUtil;

/**
 *
 * @author zygon
 */
public class RSIInterpreter extends TickerInterpreter {

    private final MovingAverage gains;
    private final MovingAverage losses;
    
    public RSIInterpreter(Aggregation aggregation) {
        super();
        
        if (aggregation.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.gains = new MovingAverage(getWindow(aggregation));
        this.losses = new MovingAverage(getWindow(aggregation));
        
        this.gains.add(0.001);
        this.losses.add(0.001);
    }

    private double lastValue = 0.0;
    
    @Override
    public Message[] interpret(Ticker data) {
        
        double price = TickerUtil.getMidPrice(data);
        boolean change = false;
        
        if (price > this.lastValue) {
            this.gains.add(price - this.lastValue);
            change = true;
        } else if (price < this.lastValue) {
            this.losses.add(this.lastValue - price);
            change = true;
        }
        
        if (change) {
            this.lastValue = price;

            double rs = this.gains.getMean() / this.losses.getMean();

            double rsi = 100 - (100 / (1 + rs));

            return new Message[] {
                new RSI(data.getTradableIdentifier(), data.getTimestamp(), rsi)
            };
        } else {
            return null;
        }
    }
}
