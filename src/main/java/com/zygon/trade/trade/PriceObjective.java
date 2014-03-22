/**
 * 
 */

package com.zygon.trade.trade;

import static com.zygon.trade.trade.TradeType.LONG;
import static com.zygon.trade.trade.TradeType.SHORT;

/**
 *
 * @author zygon
 */
public class PriceObjective {
    
    private static double UNIT_OF_CHANGE = 0.01; // TODO: allow for custom changes.
    
    public static enum Modifier {
        ADD,
        PERCENT,
        PIP
    }
    
    private static double calcProfit(TradeType type, Modifier modifier, double takeProfit, double price) {

        switch (type) {
            case LONG:
                // If we're going long then the profit is above the current price

                switch (modifier) {
                    case ADD:
                        return price + takeProfit;
                    case PERCENT:
                        return price + (price * (takeProfit / 100));
                    case PIP:
                        return price + (takeProfit * UNIT_OF_CHANGE);
                }

            case SHORT:
                // If we're going short then the profit is below the current price

                switch (modifier) {
                    case ADD:
                        return price - takeProfit;
                    case PERCENT:
                        return price - (price * (takeProfit / 100));
                    case PIP:
                        return price - (takeProfit * UNIT_OF_CHANGE);
                }
        }

        throw new IllegalStateException("Cannot calculate profit");
    }
    
    private static double calcStopLoss(TradeType type, Modifier modifier, double stopLoss, double price) {
        switch (type) {
            case LONG:
                // If we're going long then the stop loss is below the current price

                switch (modifier) {
                    case ADD:
                        return price - stopLoss;
                    case PERCENT:
                        return price - (price * (stopLoss / 100));
                    case PIP:
                        return price - (stopLoss * UNIT_OF_CHANGE);
                }

            case SHORT:
                // If we're going short then the stop loss is above the current price

                switch (modifier) {
                    case ADD:
                        return price + stopLoss;
                    case PERCENT:
                        return price + (price * (stopLoss / 100));
                    case PIP:
                        return price + (stopLoss * UNIT_OF_CHANGE);
                }
        }
        
        throw new IllegalStateException("Cannot calculate stop loss");
    }
    
    private final TradeType type;
    private final Modifier modifier;
    private final double takeProfitModifier;
    private final double stopLossModifier;
    
    private boolean priceSet = false;
    private double takeProfitValue = -1.0;
    private double stopLossValue = -1.0;
    
    public PriceObjective(TradeType type, Modifier modifier, double takeProfitModifier, double stopLossModifier, double price) {
        this.type = type;
        this.modifier = modifier;
        this.takeProfitModifier = takeProfitModifier;
        this.stopLossModifier = stopLossModifier;
        
        if (price != -1.0) {
            this.setPrice(price);
        }
    }

    public PriceObjective(TradeType type, Modifier modifier, double takeProfitModifier, double stopLossModifier) {
        this(type, modifier, takeProfitModifier, stopLossModifier, -1.0);
    }
    
    public double getStopLoss() {
        if (!this.priceSet) {
            throw new IllegalStateException("price has not been set");
        }
        return this.stopLossValue;
    }

    public double getTakeProfit() {
        if (!this.priceSet) {
            throw new IllegalStateException("price has not been set");
        }
        return this.takeProfitValue;
    }

    public final void setPrice(double price) {
        if (this.priceSet) {
            throw new IllegalStateException("Can only set price once");
        }
        if (price < 0) {
            throw new IllegalArgumentException("price must be greater than or equal to zero");
        }
        this.takeProfitValue = calcProfit(this.type, this.modifier, takeProfitModifier, price);
        this.stopLossValue = calcStopLoss(this.type, this.modifier, stopLossModifier, price);
        this.priceSet = true;
    }
}
