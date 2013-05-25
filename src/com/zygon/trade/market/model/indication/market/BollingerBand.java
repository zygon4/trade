/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 *
 * @author zygon
 */
public class BollingerBand extends MarketIndication {

    public static Identifier ID = new ID("bollinger", Classification.PRICE);
    
    private final boolean isAboveUpperBand;
    private final boolean isBelowLowerBand;
    private final boolean isAboveMovingAverage;
    private final boolean isBelowMovingAverage;
    private final boolean isNearMovingAverage;
    
    public BollingerBand(String tradableIdentifier, long timestamp, double movingAverage, double std, int kstd, double price) {
        super(ID, tradableIdentifier, timestamp);
        
        double upper = movingAverage + (kstd * std);
        double lower = movingAverage - (kstd * std);
        
        this.isAboveUpperBand = price > upper;
        this.isBelowLowerBand = price < lower;
        
        this.isAboveMovingAverage = price > movingAverage;
        this.isBelowMovingAverage = price < movingAverage;
        
        this.isNearMovingAverage = (!this.isAboveUpperBand && !this.isBelowLowerBand) &&
                                   (price <= (movingAverage + std) || price >= (movingAverage - std));
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
    
    /**
     * Returns true if the price is 1 std from the moving average AND not beyond 
     * the outer bands.  This  potentially signals an exit.
     * @return true if the price is 1 std from the moving average AND not beyond 
     * the outer bands.  This  potentially signals an exit.
     */
    public boolean isNearMovingAverage () {
        return this.isNearMovingAverage;
    }
}
