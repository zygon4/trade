/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Indication;
import java.math.BigDecimal;

/**
 *
 * @author zygon
 */
public class NumericIndication extends Indication {
    
    private final double value;
    
    public NumericIndication(Identifier id, String tradableIdentifier, long timestamp, double value) {
        super(id, tradableIdentifier, timestamp);
        this.value = value;
    }
    
    public double getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(super.toString());
        sb.append(' ');
        sb.append(this.getValue());
        
        return sb.toString();
    }
    
    public BigDecimal value() {
        return new BigDecimal(this.getValue());
    }
}
