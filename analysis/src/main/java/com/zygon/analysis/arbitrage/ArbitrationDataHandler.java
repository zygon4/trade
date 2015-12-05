package com.zygon.analysis.arbitrage;

import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data1.Data;
import com.zygon.data1.DataProvider;
import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.data.Ticker;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author zygon
 */
public class ArbitrationDataHandler implements DataProvider.DataHandler<MarketDataContract> {

    private static final class ArbData {
        private int idx = 0;
        private final BiMap<String,Integer> currencyIndex = HashBiMap.create();
        private final LinkedHashMap<Integer, List<Double>> ratesByBaseCurrency = Maps.newLinkedHashMap();

        public void add (CurrencyPair pair, double value) {
            Preconditions.checkArgument(value >= 0.0);

            Integer baseIndex = currencyIndex.get(pair.baseSymbol);
            if (baseIndex == null) {
                baseIndex = idx++;
                currencyIndex.put(pair.baseSymbol, baseIndex);

                // tbd
                List<Double> rates = Lists.newArrayList();
                for (int i = 0; i < baseIndex; i++) {
                    rates.add(i, -1.0);
                }
                rates.add(baseIndex, 1.0);
                ratesByBaseCurrency.put(baseIndex, rates);
            }

            Integer counterIndex = currencyIndex.get(pair.counterSymbol);
            if (counterIndex == null) {
                counterIndex = idx++;
                currencyIndex.put(pair.counterSymbol, counterIndex);

                // tbd
                List<Double> rates = Lists.newArrayList();
                for (int i = 0; i < counterIndex; i++) {
                    rates.add(i, -1.0);
                }
                rates.add(counterIndex, 1.0);
                ratesByBaseCurrency.put(counterIndex, rates);
            }

            List<Double> baseIndexRates = ratesByBaseCurrency.get(baseIndex);

            if (baseIndexRates.size() >= counterIndex + 1) {
                baseIndexRates.set(counterIndex, value);
            } else {
                baseIndexRates.add(counterIndex, value);
            }
        }

        public Map<String, List<Double>> getRatesByBaseCurrency() {
            Map<String, List<Double>> ratesByCurrency = Maps.newLinkedHashMap();

            BiMap<Integer, String> currencyByIndex = currencyIndex.inverse();

            for (Map.Entry<Integer,List<Double>> entry : ratesByBaseCurrency.entrySet()) {
                ratesByCurrency.put(currencyByIndex.get(entry.getKey()), entry.getValue());
            }

            return ImmutableMap.copyOf(ratesByCurrency);
        }
    }

    public static void main(String[] args) {
        ArbData data = new ArbData();

        data.add(CurrencyPair.BTC_USD, 378.49);
        data.add(CurrencyPair.BTC_EUR, 355);
        data.add(CurrencyPair.BTC_LTC, 105.05);
        data.add(new CurrencyPair("BTC", "ETH"), 434.0);

        data.add(CurrencyPair.LTC_BTC, 0.0032);
        data.add(CurrencyPair.LTC_EUR, 3.4);
    }

    public ArbitrationDataHandler() {

    }

    private final ArbData arbitrageData = new ArbData();
    private int opportunities = 0;

    @Override
    public void handle(MarketDataContract contract, Data data) {

        Ticker ticker = (Ticker) data;

        double value = ticker.getAsk().add(ticker.getBid()).divide(BigDecimal.valueOf(2.0)).doubleValue();

        arbitrageData.add(new CurrencyPair(ticker.getTradableIdentifier(), ticker.getCurrency()), value);

        Arbitrage arb = new Arbitrage(arbitrageData.getRatesByBaseCurrency());

        if(arb.printOpportunity()) {
            opportunities ++;
            System.out.print("Arbitrage opportunity at " + new Date() + ", count " + opportunities);
        }

//        boolean tValue = false;
//
//        if (contract.getCurrency().equals(correlationTarget) && contract.getContractName().equals(source + "_" + correlationTarget.toString())) {
//            correlationAnalysis.addTargetData(value, ticker.getTimestamp().getTime());
//            tValue = true;
//        } else {
//            correlationAnalysis.addCorrelateData(contract.getContractName() + "_" + contract.getCurrency().toString(),
//                    value, ticker.getTimestamp().getTime());
//        }
//
//        final boolean targetValue = tValue;
//        System.out.println(new Date() + ") received data(b/a mid) [" + value + "] from " + contract.getContractName() + "|" + contract.getDataIdentifier() + "|" + ticker.getCurrency());
////            System.out.println(getCorrelationAnalysis());
//
//        correlationAnalysis.getCorrelationsByCorrelateName().entrySet().stream()
//                .forEach(entry -> {
//                    // skipping a verification step if the correlation ranking hasn't been set (or, technically if it's actually 0)
//                    if (entry.getValue().getCorrelation() != 0.0) {
//
//                        CorrelationVerification correlateVer = verificationByCorrelate.get(entry.getKey());
//                        if (correlateVer == null) {
//                            correlateVer = new CorrelationVerification(correlationAnalysis.getTargetName(), entry.getKey());
//                            verificationByCorrelate.put(entry.getKey(), correlateVer);
//                        }
//
//                        if (targetValue) {
//                            correlateVer.setCurrentTargetPrice(value);
//                        } else {
//                            correlateVer.setTrendExpectation(entry.getValue(), value);
//
//                            System.out.println("Correlation " + entry.getKey() + ", correlation: " + entry.getValue() + ", "
//                                    + "accuracy: " + correlateVer.getAccuracyPct() + " (" + correlateVer.correctPredictions + "/" + correlateVer.totalPredictions + ")");
//                        }
//
//                        // TODO: print
////                        if (trendingUp != null) {
////                            System.out.println("Correlate " + topCorrelate.getCorrelateName() + " is trending " + (trendingUp ? "UP" : "DOWN")
////                                    + ", expect " + topCorrelate.getTargetName() + " to do the same.");
////                        }
//                    }
//                });

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
