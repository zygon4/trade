/**
 * 
 */

package com.zygon.exchange.market.model.indication.numeric;

import com.zygon.exchange.market.model.indication.Classification;
import com.zygon.exchange.market.model.indication.Aggregation;

/**
 *
 * @author zygon
 */
public class NumericIndications {
    
    public final NumericIndicationListener SMA_15_SEC;
    public final NumericIndicationListener SMA_5_MIN;
    public final NumericIndicationListener SMA_15_MIN;
    public final NumericIndicationListener SMA_30_MIN;
    public final NumericIndicationListener SMA_60_MIN;
    public final NumericIndicationListener SMA_240_MIN;
    
    public final NumericIndicationListener SMA_1_DAY;
    
    public NumericIndications(String security) {
        
        Aggregation aggregation = new Aggregation(Aggregation.Type.AVG, 15, TimeUnits.SECONDS);
        this.SMA_15_SEC = new NumericIndicationBuilder(security)
                .set(aggregation)
                .set(Classification.PRICE)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 5, TimeUnits.MINUTES);
        this.SMA_5_MIN = new NumericIndicationBuilder(security)
                .set(aggregation)
                .set(Classification.PRICE)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 15, TimeUnits.MINUTES);
        this.SMA_15_MIN = new NumericIndicationBuilder(security)
                .set(aggregation)
                .set(Classification.PRICE)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 30, TimeUnits.MINUTES); 
        this.SMA_30_MIN = new NumericIndicationBuilder(security)
                .set(aggregation)
                .set(Classification.PRICE)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 60, TimeUnits.MINUTES);
        this.SMA_60_MIN = new NumericIndicationBuilder(security)
                .set(aggregation)
                .set(Classification.PRICE)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 240, TimeUnits.MINUTES);
        this.SMA_240_MIN = new NumericIndicationBuilder(security)
                .set(aggregation)
                .set(Classification.PRICE)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 1, TimeUnits.DAYS);
        this.SMA_1_DAY = new NumericIndicationBuilder(security)
                .set(aggregation)
                .set(Classification.PRICE)
                .build();
    }
}
