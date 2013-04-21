/**
 * 
 */

package com.zygon.exchange.market.model.indication.numeric;

import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.market.model.indication.Indication;
import com.zygon.exchange.market.model.indication.IndicationListener;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class NumericIndicationListener extends IndicationListener<Numeric> {
    
    public static interface ValueStatementProvider {
        public void get(StringBuilder sb);
    }
    
    /*pkg*/ static final class DefaultValueStatementProvider implements ValueStatementProvider {
        
        @Override
        public void get(StringBuilder sb) {
            sb.append("value");
        }
    }
    
    private static String getName(Classification clazz, Aggregation aggregation) {
        return aggregation+"_"+clazz.name();
    }
    
    private ValueStatementProvider valueStmtProvider;
    private final Aggregation aggregation;
    
    public NumericIndicationListener (String security, Classification classification, 
            ValueStatementProvider valueStmtProvider, Aggregation aggregation) {
        super(getName(classification, aggregation), security, classification, Numeric.class);
        
        this.valueStmtProvider = valueStmtProvider != null ? valueStmtProvider : new DefaultValueStatementProvider();
        this.aggregation = aggregation;
    }

    public NumericIndicationListener(String security, Classification classification, 
            ValueStatementProvider valueStmtProvider, Aggregation aggregation,
            Collection<IndicationListener<? extends Indication>> listeners) {
        super (getName(classification, aggregation), 
                security, classification, Numeric.class, listeners);
        
        this.valueStmtProvider = valueStmtProvider != null ? valueStmtProvider : new DefaultValueStatementProvider();
        this.aggregation = aggregation;
    }

    @Override
    protected void appendFrom(StringBuilder sb, String clsName, String tradeableIdentifier, String classicationId) {
        super.appendFrom(sb, clsName, tradeableIdentifier, classicationId);
        
        if (this.aggregation != null) {
            sb.append(".win:ext_timed(timestamp, ");
            sb.append(this.aggregation.getDuration()).append(' ');
            sb.append(this.aggregation.getUnits()).append(')');
        }
    }
    
    @Override
    protected void appendSelectStmt(StringBuilder sb) {
        super.appendSelectStmt(sb);
        sb.append(", ");
        this.valueStmtProvider.get(sb);
    }

    public Aggregation getAggregation() {
        return aggregation;
    }
    
    // Not a huge fan of this being mutable.. just sayin'
    public void setValueStmtProvider(ValueStatementProvider valueStmtProvider) {
        this.valueStmtProvider = valueStmtProvider;
    }
}
