/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.strategy.IndicationProcessor;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 * 
 * A convenience collection of indication listeners.
 */
public class NumericIndications {
    
    public final NumericIndicationListener SMA_15_SEC;
    public final NumericIndicationListener SMA_5_MIN;
    public final NumericIndicationListener SMA_15_MIN;
    public final NumericIndicationListener SMA_30_MIN;
    public final NumericIndicationListener SMA_60_MIN;
    public final NumericIndicationListener SMA_240_MIN;
    
    public final NumericIndicationListener SMA_1_DAY;
    
    public NumericIndications(String security, IndicationProcessor<NumericIndication> processor) {
        
        Aggregation aggregation = new Aggregation(Aggregation.Type.AVG, 15, TimeUnit.SECONDS);
        this.SMA_15_SEC = new NumericIndicationBuilder()
                .set(aggregation)
                .set(Classification.PRICE)
                .set(processor)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 5, TimeUnit.MINUTES);
        this.SMA_5_MIN = new NumericIndicationBuilder()
                .set(aggregation)
                .set(Classification.PRICE)
                .set(processor)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 15, TimeUnit.MINUTES);
        this.SMA_15_MIN = new NumericIndicationBuilder()
                .set(aggregation)
                .set(Classification.PRICE)
                .set(processor)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 30, TimeUnit.MINUTES); 
        this.SMA_30_MIN = new NumericIndicationBuilder()
                .set(aggregation)
                .set(Classification.PRICE)
                .set(processor)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 60, TimeUnit.MINUTES);
        this.SMA_60_MIN = new NumericIndicationBuilder()
                .set(aggregation)
                .set(Classification.PRICE)
                .set(processor)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 240, TimeUnit.MINUTES);
        this.SMA_240_MIN = new NumericIndicationBuilder()
                .set(aggregation)
                .set(Classification.PRICE)
                .set(processor)
                .build();
        
        aggregation = new Aggregation(Aggregation.Type.AVG, 1, TimeUnit.DAYS);
        this.SMA_1_DAY = new NumericIndicationBuilder()
                .set(aggregation)
                .set(Classification.PRICE)
                .set(processor)
                .build();
    }
}
