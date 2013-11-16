
package com.zygon.trade.agent;

import com.zygon.trade.market.util.MovingAverage;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunctionLagrangeForm;

/**
 * TODO: this is just some data to be used for analysis later on.. no real use
 * now
 *
 * @author zygon
 */
public class AnalysisAgent {

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
    
    public static void main(String[] args) {
        int window = 2;
        int predictionLength = 1;
        UnivariateFunction interp = null;
        
        double[] xVals = new double[window];
        for (int i = 0; i < xVals.length; i++) {
            xVals[i] = i+1;
        }
        
        MovingAverage ma = new MovingAverage(window);
        
        double totalError = 0.0;
        
        for (int i = 0; i < VALS.length; i++) {
            
            double d = VALS[i];
            ma.add(d);
            
            if (ma.getAvailableValues() == window) {
                interp = new PolynomialFunctionLagrangeForm(xVals, ma.getValues());
//                interp = new LinearInterpolator().interpolate(xVals, ma.getValues());
                
                double predictedValue = interp.value(window + predictionLength);
                System.out.println(predictedValue);
                
                if (i < VALS.length - predictionLength) {
                    double error = VALS[i+predictionLength] - predictedValue;
                    totalError += Math.abs(error);
//                    System.out.println("Error:" + error);
                }
            }
        }
        
        System.out.println("Total Error: " + totalError);
        
    }
}
