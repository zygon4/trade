/**
 * 
 */

package com.zygon.exchange.market.model.indication.numeric;

import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.exchange.market.model.indication.Aggregation;
import com.zygon.exchange.market.model.indication.Indication;
import com.zygon.exchange.market.model.indication.IndicationListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author zygon
 */
public class NumericIndicationListener extends IndicationListener<Numeric> {
    
    public static interface ValueStatementProvider {
        public void get(StringBuilder sb);
    }
    
    // This might actually be a horribly inefficient way of aggregrating information
    private static final class DefaultValueStatementProvider implements ValueStatementProvider {
    
        private final NumericIndicationListener[] movingAverages;
        private final String valueString;
        
        public DefaultValueStatementProvider(NumericIndicationListener[] movingAverages) {
            this.movingAverages = movingAverages;
            
            if (movingAverages != null) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < this.movingAverages.length; i++) {
                    sb.append(getListenerValueKey(this.movingAverages[i]));
                    if (i < this.movingAverages.length - 1) {
                        sb.append(", ");
                    }
                }
                
                this.valueString = sb.toString();
            } else {
                this.valueString = "value";
            }
        }
        
        public DefaultValueStatementProvider() {
            this (null);
        }

        @Override
        public void get(StringBuilder sb) {
            sb.append(this.valueString);
        }
    }
    
    private static Collection<IndicationListener<? extends Indication>> get(NumericIndicationListener[] listeners) {
        List<IndicationListener<? extends Indication>> foo = new ArrayList<>();
        Collections.addAll(foo, listeners);
        return foo;
    }
    
    protected static String getListenerValueKey(NumericIndicationListener listener) {
        return String.format("%s(%s.value)", 
                listener.getAggregation().getType().getVal(), listener.getName());
    }
    
    protected static String getListenerTimestampKey(NumericIndicationListener listener) {
        return String.format("%s.timestamp", listener.getName());
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
            NumericIndicationListener[] listeners) {
        super (getName(classification, aggregation), 
                security, classification, Numeric.class, get(listeners));
        
        this.valueStmtProvider = valueStmtProvider != null ? valueStmtProvider : new DefaultValueStatementProvider(listeners);
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
