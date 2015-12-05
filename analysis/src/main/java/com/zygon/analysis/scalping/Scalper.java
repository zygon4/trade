
package com.zygon.analysis.scalping;

import com.google.common.base.Function;
import com.xeiam.xchange.dto.trade.LimitOrder;
import com.zygon.analysis.core.OrderGenerator;
import com.zygon.analysis.util.Duration;
import com.zygon.analysis.util.MovingAverage;
import com.zygon.data1.data.OpenOrderBook;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class Scalper implements OrderGenerator {

    private final MovingAverage askPriceMA = new MovingAverage(Duration._1, TimeUnit.MINUTES);
    private final MovingAverage askVolMA = new MovingAverage(Duration._1, TimeUnit.MINUTES);

    private final MovingAverage bidPriceMA = new MovingAverage(Duration._1, TimeUnit.MINUTES);
    private final MovingAverage bidVolMA = new MovingAverage(Duration._1, TimeUnit.MINUTES);


    @Override
    public Collection<LimitOrder> generateOrders(OpenOrderBook book) {

//        book.getAsks().forEach(a -> System.out.println(a.getTradableAmount().doubleValue() + "@" + a.getLimitPrice().doubleValue()) );
//        book.getBids().forEach(a -> System.out.println(a.getTradableAmount().doubleValue() + "@" + a.getLimitPrice().doubleValue()) );

        askPriceMA.add(getBestAvg(book.getAsks(), (LimitOrder input) -> input.getLimitPrice().doubleValue()));
        askVolMA.add(getBestAvg(book.getAsks(), (LimitOrder input) -> input.getTradableAmount().doubleValue()));

        bidPriceMA.add(getBestAvg(book.getBids(), (LimitOrder input) -> input.getLimitPrice().doubleValue()));
        bidVolMA.add(getBestAvg(book.getBids(), (LimitOrder input) -> input.getTradableAmount().doubleValue()));

        double bestAsk = getBest(book.getAsks(), (LimitOrder input) -> input.getLimitPrice().doubleValue());
        double bestBid = getBest(book.getBids(), (LimitOrder input) -> input.getLimitPrice().doubleValue());


        System.out.println("--------------------------------------------------");
        System.out.println("---BOOK---" + new Date() + "---------------");
        System.out.println("ASK: " + bestAsk + ", BID: " + bestBid + ", SPREAD: " + (bestAsk - bestBid));
        System.out.println("Avg Spread: " + (askPriceMA.getMean() - bidPriceMA.getMean()));
        System.out.println("Avg ASK vol: " + askVolMA.getMean());
        System.out.println("Avg BID vol: " + bidVolMA.getMean());
        System.out.println("--------------------------------------------------");
        System.out.println();

        return Collections.emptyList();
    }

    private double getBest(Collection<LimitOrder> orders, Function<LimitOrder, Double> getValue) {
        return orders.stream()
                .limit(1)
                .mapToDouble(lo -> getValue.apply(lo))
                .sum(); // having to sum is kinda dumb here
    }

    private double getBestAvg(Collection<LimitOrder> orders, Function<LimitOrder, Double> getValue) {
        return orders.stream()
                .limit(4)
                .mapToDouble(lo -> getValue.apply(lo))
                .average()
                .getAsDouble();
    }
}
