/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.Message;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.market.BollingerBand;
import com.zygon.trade.market.util.ExponentialMovingAverage;
import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.util.TickerUtil;

/**
 *
 * @author zygon
 */
public class BBInterpreter extends TickerInterpreter {

    private final MovingAverage ema;
    private final int kstd;

    public BBInterpreter(Aggregation ma, int kstd) {
        super();
        if (ma.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.ema = new ExponentialMovingAverage(getWindow(ma));
        this.kstd = kstd;
    }
    
    @Override
    public Message[] interpret(Ticker data) {
        
        double price = TickerUtil.getMidPrice(data);
        
        this.ema.add(price);
        
        double ma = this.ema.getMean();
        double std = this.ema.getStd();
        
        Message bb = new BollingerBand(data.getTradableIdentifier(), data.getTimestamp(), ma, std, this.kstd, price);
        
        return new Message[]{bb};
    }
}