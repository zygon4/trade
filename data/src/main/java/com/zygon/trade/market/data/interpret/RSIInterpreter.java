/**
 *
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.util.Aggregation;
import com.zygon.trade.market.model.indication.numeric.RSI;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerUtil;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.util.Type;

/**
 *
 * @author zygon
 */
public class RSIInterpreter extends TickerInterpreter {

    private final MovingAverage gains;
    private final MovingAverage losses;

    public RSIInterpreter(MovingAverage gains, MovingAverage losses) {
        super();
        this.gains = gains;
        this.losses = losses;
    }

    public RSIInterpreter(Aggregation aggregation) {
        this(new MovingAverage(aggregation.getDuration(), aggregation.getUnits()),
             new MovingAverage(aggregation.getDuration(), aggregation.getUnits()));

        if (aggregation.getType() != Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
    }

    private double lastValue = -1.0;

    @Override
    public Indication[] interpret(Ticker data) {

        double price = TickerUtil.getMidPrice(data);

        if (this.lastValue == -1.0) {
            this.lastValue = price;
        }

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

            double gainsAvg = this.gains.getMean();
            double lossesAvg = this.losses.getMean();
            if (!Double.isNaN(gainsAvg) && !Double.isNaN(lossesAvg)) {

                double rs = gainsAvg / lossesAvg;

                double rsi = 100 - (100 / (1 + rs));

                return new Indication[] {
                    new RSI(data.getTradableIdentifier(), data.getTimestamp().getTime(), rsi)
                };
            }
        }

        return null;
    }
}
