
package com.zygon.trade.agent;

import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.numeric.Price;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 * @author zygon
 */
public class StrategyTester {

    private static final double[] VALS = {
        188.17255
        ,188.17255
        ,188.17255
        ,189.06998
        ,189.48498
        ,189.48498
        ,189.48498
        ,189.48498
        ,189.48498
        ,189.48498
        ,188.60499
        ,188.60749
        ,188.61
        ,188.6125
        ,188.61501
        ,188.62252
        ,188.60499
        ,188.63507
        ,188.64258
        ,188.6501
        ,188.41512
        ,188.42263
        ,188.42764
        ,188.43515
        ,188.44017
        ,188.44519
        ,188.45522
        ,188.45772
        ,188.46274
        ,188.47025
        ,188.47527
        ,188.48027
        ,188.48529
        ,188.17255
        ,188.56503
        ,189.06748
        ,189.06998
        ,189.48498
        ,189.48498
        ,189.48498
        ,189.48498
        ,189.48498
        ,189.48498
        ,188.60499
        ,188.60749
        ,188.61
        ,188.6125
        ,188.61501
        ,188.62252
        ,188.60499
        ,188.63507
        ,188.64258
        ,188.6501
        ,188.41512
        ,188.42263
        ,188.42764
        ,188.43515
        ,188.44017
        ,188.44519
        ,188.45522
        ,188.45772
        ,188.46274
        ,188.47025
        ,188.47527
        ,188.48027
        ,188.48529
    };
    
    private final Collection<Identifier> testIdentifiers = new ArrayList<Identifier>(Arrays.asList(Price.ID));
    
    private final Strategy testStrategy = new Strategy(
            "test", 
            this.testIdentifiers, 
            null);
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        
        StrategyTester tester = new StrategyTester();
        
        tester.testStrategy.start();
        try {
            
            for (int i = 0; i < VALS.length; i++) {
                tester.testStrategy.send(new Price("BTC", System.currentTimeMillis(), VALS[i], "USD"));
            }
            
        } finally {
            tester.testStrategy.stop();
        }
    }

}
