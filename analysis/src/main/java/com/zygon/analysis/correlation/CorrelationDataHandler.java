
package com.zygon.analysis.correlation;

import com.google.common.collect.Maps;
import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data1.Data;
import com.zygon.data1.DataProvider;
import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.data.Ticker;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class CorrelationDataHandler implements DataProvider.DataHandler<MarketDataContract>{

    private static final class CorrelationVerification {
        private final String targetName;
        private double currentTargetPrice;

        private final String correlateName;
        private double lastCorrelatePrice;

        private Boolean expectTrendUp = null;
        private double totalPredictions = 0;
        private double correctPredictions = 0;

        public CorrelationVerification(String targetName, String topCorrelateName) {
            this.targetName = targetName;
            this.correlateName = topCorrelateName;
        }

        public double getAccuracyPct() {
            double accuracy = totalPredictions > 0 ? (correctPredictions / totalPredictions) : 0;
            return accuracy *= 100.0;
        }

        public void setCurrentTargetPrice(double currentTargetPrice) {

            if (currentTargetPrice == this.currentTargetPrice) {
                // Avoid false-negatives. If we expect a change, it should
                // still happen, but if the price remains the same for a while
                // that's probably OK.
                return;
            }

            if (this.expectTrendUp != null) {
                if (expectTrendUp) {
                    if (currentTargetPrice > this.currentTargetPrice) {
                        correctPredictions ++;
                    }
                } else{
                    if (currentTargetPrice < this.currentTargetPrice) {
                        correctPredictions ++;
                    }
                }

                this.totalPredictions ++;
            }

            this.currentTargetPrice = currentTargetPrice;
        }

        public void setTrendExpectation(Correlation correlation, double correlatePrice) {

            expectTrendUp = null;

            if (correlation.isSignificant()) {
                // highly correlated, so if the correlate goes up, we go up
                if (correlatePrice > lastCorrelatePrice) {
                    expectTrendUp = true;
                } else if (correlatePrice < lastCorrelatePrice) {
                    expectTrendUp = false;
                }
            }

            lastCorrelatePrice = correlatePrice;
        }
    }

    private final CorrelationAnalysis correlationAnalysis;
    private final CurrencyPair correlationTarget;
    private final String source;

    public CorrelationDataHandler(CurrencyPair correlationTarget, String source) {
        this.correlationAnalysis = new CorrelationAnalysis(source + "_" + correlationTarget.toString());
        this.correlationTarget = correlationTarget;
        this.source = source;
    }

    public CorrelationAnalysis getCorrelationAnalysis() {
        return correlationAnalysis;
    }

    // TODO: hacking in some ghetto up/down analysis - move!
    private final Map<String,CorrelationVerification> verificationByCorrelate = Maps.newHashMap();

    @Override
    public void handle(MarketDataContract contract, Data data) {

        Ticker ticker = (Ticker) data;
        double value = ticker.getAsk().add(ticker.getBid()).divide(BigDecimal.valueOf(2.0)).doubleValue();
        boolean tValue = false;

        if (contract.getCurrency().equals(correlationTarget) && contract.getContractName().equals(source+"_"+correlationTarget.toString())) {
            correlationAnalysis.addTargetData(value, ticker.getTimestamp().getTime());
            tValue = true;
        } else {
            correlationAnalysis.addCorrelateData(contract.getContractName()+"_"+contract.getCurrency().toString(),
                    value, ticker.getTimestamp().getTime());
        }

        final boolean targetValue = tValue;
        System.out.println(new Date() + ") received data(b/a mid) [" + value + "] from " + contract.getContractName() + "|"+ contract.getDataIdentifier() + "|" + ticker.getCurrency());
//            System.out.println(getCorrelationAnalysis());

        correlationAnalysis.getCorrelationsByCorrelateName().entrySet().stream()
                .forEach(entry -> {
                    // skipping a verification step if the correlation ranking hasn't been set (or, technically if it's actually 0)
                    if (entry.getValue().getCorrelation() != 0.0) {

                        CorrelationVerification correlateVer = verificationByCorrelate.get(entry.getKey());
                        if (correlateVer == null) {
                            correlateVer = new CorrelationVerification(correlationAnalysis.getTargetName(), entry.getKey());
                            verificationByCorrelate.put(entry.getKey(), correlateVer);
                        }

                        if (targetValue) {
                            correlateVer.setCurrentTargetPrice(value);
                        } else {
                            correlateVer.setTrendExpectation(entry.getValue(), value);

                            System.out.println("Correlation " + entry.getKey() + ", correlation: " + entry.getValue() + ", "
                                    + "accuracy: " + correlateVer.getAccuracyPct() + " (" + correlateVer.correctPredictions + "/" + correlateVer.totalPredictions + ")");
                        }

                        // TODO: print
//                        if (trendingUp != null) {
//                            System.out.println("Correlate " + topCorrelate.getCorrelateName() + " is trending " + (trendingUp ? "UP" : "DOWN")
//                                    + ", expect " + topCorrelate.getTargetName() + " to do the same.");
//                        }

                    }
                });

//        Correlation topCorrelate = correlationAnalysis.getTopCorrelate();
//        if (topCorrelate.getCorrelation() != 0.0) {
//            if (value > topCorrelateLast) {
//                trendingUp = true;
//            } else if (value < topCorrelateLast) {
//                trendingUp = false;
//            } else {
//                trendingUp = null;
//            }
//
//            if (trendingUp != null) {
//                System.out.println("Correlate " + topCorrelate.getCorrelateName() + " is trending " + (trendingUp ? "UP" : "DOWN")
//                        + ", expect " + topCorrelate.getTargetName() + " to do the same.");
//            }
//
//            // TBD: ghetto
//            topCorrelateLast = value;
//        }
    }
}
