
package com.zygon.trade.market.data.interpret;

import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.stat.descriptive.moment.Mean;

/**
 *
 * @author zygon
 */
public class StatTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Mean mean = new Mean();
        ChiSquaredDistribution dist = new ChiSquaredDistribution(1);
        
        for (int j = 0; j < 250; j++) {
        
            for (int i = 0; i < 10000; i++) {
                mean.increment(dist.sample());
            }
        
            System.out.println(mean.getResult());
        }
        
        
    }

}
