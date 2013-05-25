/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.trade.market.model.indication.Aggregation;
import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.InformationManager;
import com.zygon.trade.market.model.indication.numeric.NumericIndication;
import com.xeiam.xchange.Currencies;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.junit.Test;
        
/**
 *
 * @author zygon
 */
public class InformationManagerTest {

    @Test
    public void test() {
    
//    public static void main(String[] args) {
//        List<IndicationListener> indications = new ArrayList<>();
////        indications.add(new IndicationListener(Currencies.USD, new Selector(), new Aggregation(Aggregation.Type.AVG, Aggregation.Duration._1, TimeUnit.SECONDS)));
//        
//        InformationManager mgmt = new InformationManager("info-mgmt", indications);
//        
//        Random rand = new Random(System.currentTimeMillis());
//        
//        for (int i = 0; i < 100; i++) {
//            long price = rand.nextInt(50) + 50;
//            NumericIndication p = new NumericIndication(Currencies.BTC, Classification.PRICE, System.currentTimeMillis(), price);
//            mgmt.handle(p);
//        }
    }
}
