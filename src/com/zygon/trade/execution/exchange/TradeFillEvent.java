
package com.zygon.trade.execution.exchange;

/**
 *
 * @author david.charubini
 */
public class TradeFillEvent extends TradeEvent {

    public static enum Fill {
        FULL,
        PARTIAL
    }
    
    private final Fill fill;
    private final double fillAmmount;
    
    public TradeFillEvent(long tradeID, Fill fill, double fillAmmount) {
        super(tradeID);
        
        if (fillAmmount < 0.0) {
            throw new IllegalArgumentException("fill ammount cannot be negative");
        }
        
        this.fill = fill;
        this.fillAmmount = fillAmmount;
    }

    public Fill getFill() {
        return this.fill;
    }

    public double getFillAmmount() {
        return this.fillAmmount;
    }
}
