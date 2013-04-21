/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.market.model.indication.numeric.Numeric;
import com.zygon.exchange.market.model.indication.numeric.NumericIndicationListener;
import com.xeiam.xchange.Currencies;
import com.zygon.exchange.market.model.indication.numeric.TimeUnits;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.Test;
        
/**
 *
 * @author zygon
 */
public class InformationManagerTest {

    @Test
    public void test() {
    
//    public static void main(String[] args) {
        List<IndicationListener> indications = new ArrayList<>();
        indications.add(new NumericIndicationListener(Currencies.USD, Classification.PRICE, null, new Aggregation(Aggregation.Type.AVG, 1, TimeUnits.SECONDS)));
        
        InformationManager mgmt = new InformationManager("info-mgmt", indications);
        
        Random rand = new Random(System.currentTimeMillis());
        
        for (int i = 0; i < 100; i++) {
            long price = rand.nextInt(50) + 50;
            Numeric p = new Numeric(Currencies.BTC, Classification.PRICE.getId(), price, System.currentTimeMillis());
            mgmt.handle(p);
        }
    }
}
