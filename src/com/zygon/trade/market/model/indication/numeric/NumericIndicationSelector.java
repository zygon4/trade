/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.Selector;

/**
 *
 * @author zygon
 */
public class NumericIndicationSelector extends Selector<NumericIndication> {

    private final Aggregation aggregation;

    public NumericIndicationSelector(Identifier id, Aggregation aggregation, Classification classification) {
        super(id, classification);
        this.aggregation = aggregation;
    }
    
    @Override
    public boolean select(NumericIndication input) {
        if (super.select(input)) {
            if ((this.aggregation == null && input.getAggregation() == null) ||
                 (this.aggregation != null && this.aggregation.isEqual(input.getAggregation()))) {
                return true;
            }
        }
        
        return false;
    }
}
