/**
 * 
 */

package com.zygon.trade.trade;

/**
 *
 * @author zygon
 */
public class PriceObjective {
    private final double takeProfit;
    private final double stopLoss;

    public PriceObjective(double takeProfit, double stopLoss) {
        this.takeProfit = takeProfit;
        this.stopLoss = stopLoss;
    }

    public double getStopLoss() {
        return this.stopLoss;
    }

    public double getTakeProfit() {
        return this.takeProfit;
    }
}
