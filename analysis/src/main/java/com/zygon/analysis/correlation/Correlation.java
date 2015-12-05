
package com.zygon.analysis.correlation;

import com.zygon.trade.market.util.Duration;
import com.zygon.trade.market.util.MovingAverage;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 *
 * @author zygon
 */
public class Correlation {

    /*
    So i want to find out how each feed correlates (or not) to the average price -
    I think.

    I need: the average price of the target data
            the price(s) of the possible-correlates

    if the possible-correlates match to a high degree, then if the target
    goes up/down, expect the correlates to follow (if it hasn't already).
    */

    private final double CORRELATION_THRESHOLD = 0.90;

//    private final SpearmansCorrelation correlationAlgo = new SpearmansCorrelation();
    private final PearsonsCorrelation correlationAlgo = new PearsonsCorrelation();

    // TBD: exposing these durations
    private final MovingAverage targetData = new MovingAverage(Duration._30, TimeUnit.MINUTES);
    private final MovingAverage correlateData = new MovingAverage(Duration._30, TimeUnit.MINUTES);

    private final String targetName;
    private final String correlateName;

    public Correlation(String targetName, String correlateName) {
        this.targetName = targetName;
        this.correlateName = correlateName;
    }

    public void addData(double target, double correlate, long time) {
        addTargetData(target, time);
        addCorrelateData(correlate, time);
    }

    public void addTargetData(double target, long targetDataTime) {
        targetData.add(target, new Date(targetDataTime));
    }

    public void addCorrelateData(double correlate, long correlateDataTime) {
        correlateData.add(correlate, new Date(correlateDataTime));
    }

    public double getCorrelation() {

        double[] targetValues = targetData.getValues();
        double[] correlateValues = correlateData.getValues();

        int maxLength = Math.min(targetValues.length, correlateValues.length);
        if (maxLength < 2) {
            return 0.0;
        }

        if (targetValues.length != correlateValues.length) {
            // Need to trim down to the smaller size array
            targetValues = Arrays.copyOf(targetValues, maxLength);
            correlateValues = Arrays.copyOf(correlateValues, maxLength);
        }

        double correlation = correlationAlgo.correlation(targetValues, correlateValues);
        return Double.isNaN(correlation) ? 0.0 : correlation;
    }

    public final String getTargetName() {
        return targetName;
    }

    public final String getCorrelateName() {
        return correlateName;
    }

    public final boolean isSignificant() {
        return getCorrelation() >= CORRELATION_THRESHOLD;
    }
    @Override
    public String toString() {
        return String.valueOf(getCorrelation());
    }
}
