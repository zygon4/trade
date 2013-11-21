
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
    private final double fillPrice;
    private final double fillAmmount;
    
    public TradeFillEvent(String tradeID, Fill fill, double fillPrice, double fillAmmount) {
        super(tradeID);
        
        if (fillAmmount < 0.0) {
            throw new IllegalArgumentException("fill ammount cannot be negative");
        }
        
        this.fill = fill;
        this.fillPrice = fillPrice;
        this.fillAmmount = fillAmmount;
    }

    @Override
    public String getDisplayString() {
        String display = super.getDisplayString();
        
        display += String.format(":[%s - @%f, volume: %f", this.fill.name(), this.fillPrice, this.fillAmmount);
        
        return display;
    }

    public Fill getFill() {
        return this.fill;
    }

    public double getFillAmmount() {
        return this.fillAmmount;
    }

    public double getFillPrice() {
        return this.fillPrice;
    }
}
