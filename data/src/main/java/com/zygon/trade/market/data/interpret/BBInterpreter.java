/**
 *
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.util.Aggregation;
import com.zygon.trade.market.model.indication.market.BollingerBand;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.data.TickerUtil;
import com.zygon.trade.market.model.indication.Indication;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.util.Type;

/**
 *
 * @author zygon
 */
public class BBInterpreter extends TickerInterpreter {

    private final MovingAverage ema;
    private final int kstd;

    public BBInterpreter(Aggregation ma, int kstd) {
        super();
        if (ma.getType() != Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }

        this.ema = new MovingAverage(ma.getDuration(), ma.getUnits(), new MovingAverage.ExponentialValueFn());
        this.kstd = kstd;
    }

    @Override
    public Indication[] interpret(Ticker data) {

        double price = TickerUtil.getMidPrice(data);

        this.ema.add(price, data.getTimestamp());

        double ma = this.ema.getMean();
        double std = this.ema.getStd();

        if (!Double.isNaN(ma) && !Double.isNaN(std)) {
            Indication bb = new BollingerBand(data.getTradableIdentifier(), data.getTimestamp().getTime(), ma, std, this.kstd, price);
            return new Indication[]{bb};
        } else {
            return null;
        }
    }
}
