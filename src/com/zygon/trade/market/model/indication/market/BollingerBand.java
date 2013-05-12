/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class BollingerBand extends MarketIndication {

    private final boolean isAboveUpperBand;
    private final boolean isBelowLowerBand;
    private final boolean isAboveMovingAverage;
    private final boolean isBelowMovingAverage;
    
    public BollingerBand(String tradableIdentifier, long timestamp, double movingAverage, double std, int kstd, double price) {
        super(tradableIdentifier, Classification.PRICE, timestamp, Type.BOLLINGER_BAND);
        
        double upper = movingAverage + (kstd * std);
        double lower = movingAverage - (kstd * std);
        
        this.isAboveUpperBand = price > upper;
        this.isBelowLowerBand = price < lower;
        
        this.isAboveMovingAverage = price > movingAverage;
        this.isBelowMovingAverage = price < movingAverage;
    }

    public boolean isIsAboveMovingAverage() {
        return this.isAboveMovingAverage;
    }

    public boolean isIsAboveUpperBand() {
        return this.isAboveUpperBand;
    }

    public boolean isIsBelowLowerBand() {
        return this.isBelowLowerBand;
    }

    public boolean isIsBelowMovingAverage() {
        return this.isBelowMovingAverage;
    }
}
