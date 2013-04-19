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
public class NumericIndications {
    
    public final IndicationListener<Numeric> AVG_PRICE_5_MIN;
    public final IndicationListener<Numeric> AVG_PRICE_15_MIN;
    public final IndicationListener<Numeric> AVG_PRICE_30_MIN;
    public final IndicationListener<Numeric> AVG_PRICE_60_MIN;
    
    public NumericIndications(String security) {
        
        this.AVG_PRICE_5_MIN = new NumericIndicationBuilder(security)
                .set(new Aggregation(Aggregation.Type.AVG, 5, TimeUnits.MINUTES))
                .set(Classification.PRICE)
                .build();
        
        this.AVG_PRICE_15_MIN = new NumericIndicationBuilder(security)
                .set(new Aggregation(Aggregation.Type.AVG, 15, TimeUnits.MINUTES))
                .set(Classification.PRICE)
                .build();
        
        this.AVG_PRICE_30_MIN = new NumericIndicationBuilder(security)
                .set(new Aggregation(Aggregation.Type.AVG, 30, TimeUnits.MINUTES))
                .set(Classification.PRICE)
                .build();
        
        this.AVG_PRICE_60_MIN = new NumericIndicationBuilder(security)
                .set(new Aggregation(Aggregation.Type.AVG, 60, TimeUnits.MINUTES))
                .set(Classification.PRICE)
                .build();
    }
}
