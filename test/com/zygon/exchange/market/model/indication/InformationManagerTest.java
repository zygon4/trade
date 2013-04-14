/**
 * 
 */

package com.zygon.exchange.market.model.indication;

import com.zygon.exchange.market.model.indication.technical.NumericIndication;
import com.zygon.exchange.market.Price;
import com.xeiam.xchange.Currencies;
import com.zygon.exchange.market.model.indication.technical.Aggregation;
import com.zygon.exchange.market.model.indication.technical.Classification;
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
        List<Indication> indications = new ArrayList<>();
        indications.add(new NumericIndication(Currencies.USD, Classification.PRICE, Aggregation.AVG, 1, TimeUnit.SECONDS));
        
        InformationManager mgmt = new InformationManager("info-mgmt", indications);
        
        Random rand = new Random(System.currentTimeMillis());
        
        for (int i = 0; i < 100; i++) {
            long price = rand.nextInt(50) + 50;
            Price p = new Price(Currencies.BTC, price, System.currentTimeMillis());
            mgmt.handle(p);
        }
    }
}
