/**
 * 
 */

package com.zygon.trade.market.data.interpret;

import com.zygon.trade.market.util.MovingAverage;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.market.MACD;
import com.zygon.trade.market.model.indication.market.MACDSignalCross;
import com.zygon.trade.market.model.indication.market.MACDZeroCross;
import com.zygon.trade.market.util.ExponentialMovingAverage;
import com.zygon.trade.market.data.Ticker;
import com.zygon.trade.market.util.TickerUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author zygon
 */
public class TickerMACD extends TickerInterpreter {
    
    private final Aggregation leading;
    private final Aggregation lagging;
    private final Aggregation macd;
    
    private final MovingAverage leadingMA;
    private final MovingAverage laggingMA;
    private final MovingAverage macdMA;
    
    public TickerMACD(Aggregation leading, Aggregation lagging, Aggregation macd) {
        super();
        
        if (leading.getType() != Aggregation.Type.AVG || lagging.getType() != Aggregation.Type.AVG || macd.getType() != Aggregation.Type.AVG) {
            throw new IllegalArgumentException("Aggregations must be based on average");
        }
        
        this.leading = leading;
        this.lagging = lagging;
        this.macd = macd;
        
        this.leadingMA = new ExponentialMovingAverage(getWindow(this.leading));
        this.laggingMA = new ExponentialMovingAverage(getWindow(this.lagging));
        
        this.macdMA = new ExponentialMovingAverage(getWindow(this.macd));
    }
    
    private boolean firstValue = true;
    private boolean aboveZero = false;
    private boolean aboveSignal = false;
    
    @Override
    public MACD[] interpret(Ticker in) {
        
        //TBD: only get the average price every x number of ticks?
        
        this.leadingMA.add(TickerUtil.getMidPrice(in));
        this.laggingMA.add(TickerUtil.getMidPrice(in));
        
        double leadingPrice = this.leadingMA.getMean();
        double laggingPrice = this.laggingMA.getMean();
        
        double macdLine = leadingPrice - laggingPrice;
        
        this.macdMA.add(macdLine);
        
        double signalLine = this.macdMA.getMean();
        
        List<MACD> macds = new ArrayList<>();
        
        if (this.firstValue) {
            // First calc zero cross
            this.aboveZero = macdLine > 0.0;
            
            // Now calc signal cross
            this.aboveSignal = signalLine > macdLine;
            
            this.firstValue = false;
        } else {
            if (this.aboveZero) {
                if (macdLine < 0.0) {
                    this.aboveZero = false;
                    macds.add(new MACDZeroCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveZero));
                }
            } else {
                if (macdLine > 0.0) {
                    this.aboveZero = true;
                    macds.add(new MACDZeroCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveZero));
                }
            }
            
            if (this.aboveSignal) {
                if (signalLine < macdLine) {
                    this.aboveSignal = false;
                    macds.add(new MACDSignalCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveSignal));
                }
            } else {
                if (signalLine > macdLine) {
                    this.aboveSignal = true;
                    macds.add(new MACDSignalCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveSignal));
                }
            }
        }
        
//        // TODO: need to not generate signal unless there is a cross
//        MACD macdZeroCross = new MACDZeroCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveZero);
//        
//        // TODO: need to not generate signal unless there is a cross
//        MACD macdSignalCross = new MACDSignalCross(in.getTradableIdentifier(), in.getTimestamp(), this.aboveSignal);
        
        return macds.toArray(new MACD[macds.size()]);
    }
}
