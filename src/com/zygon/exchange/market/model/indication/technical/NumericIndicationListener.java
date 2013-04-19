/**
 * 
 */

package com.zygon.exchange.market.model.indication.technical;

import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.market.model.indication.IndicationListener;

/**
 *
 * @author zygon
 */
public class NumericIndicationListener extends IndicationListener<Numeric> {
    
    private static String getName(Classification clazz, Aggregation aggregation) {
        return aggregation+"_"+clazz.name();
    }
    
    public static String createStatement (Numeric numeric) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("select ");
        
        sb.append("tradableIdentifier, ");
        
        if (numeric.getAggregation() != null) {
            sb.append(numeric.getAggregation().getType().getVal());
        }
        if (numeric.getAggregation() != null) {
            sb.append('(');
        }
        sb.append("value");
        
        if (numeric.getAggregation() != null) {
            sb.append(')');
        }
        
        sb.append(", timestamp");
        
        sb.append(" from ");
        
        numeric.getStatement(sb, null);
        
        return sb.toString();
    }
    
    private final Numeric ref;
    private final String stmt;
    
    public NumericIndicationListener (String security, Classification classification, Aggregation aggregation) {
        super(getName(classification, aggregation));
        
        this.ref = new Numeric(security, classification.getId(), -1, -1, aggregation);
        this.stmt = createStatement(this.ref);
    }

    @Override
    public Numeric getReferenceIndication() {
        return this.ref;
    }

    @Override
    public String getStatement() {
        return this.stmt;
    }
}
