package com.zygon.analysis.arbitrage;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author zygon
 */
public class Arbitrage {

    // given any currency pairs, produce a set of arbitrage opportunities and
    // (ideally) a "confidence" level based on current volumes and price trends.
    // this class cannot be instantiated

    private final Map<String, List<Double>> ratesByCurrency;

    public Arbitrage(Map<String, List<Double>> ratesByCurrency) {
        this.ratesByCurrency = ratesByCurrency;
    }


    public boolean printOpportunity() {
        int V = ratesByCurrency.size();
        String[] name = new String[V];

        // create complete network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);

        Iterator<String> iterator = ratesByCurrency.keySet().iterator();
        int v = 0;
        while (iterator.hasNext()) {
            String currency = iterator.next();
            name[v] = currency;
            List<Double> rates = ratesByCurrency.get(currency);
            for (int w = 0; w < rates.size(); w++) {
                if (rates.get(w) != -1.0) {
                    DirectedEdge de = new DirectedEdge(v, w, -Math.log(rates.get(w)));
                    G.addEdge(de);
                }
            }
        }

        // find negative cycle
        BellmanFordSP spt = new BellmanFordSP(G, 0);
        if (spt.hasNegativeCycle()) {
            double stake = 1000.0;
            for (DirectedEdge e : spt.negativeCycle()) {
                System.out.printf("%s: %10.5f %s ", new Date(), stake, name[e.from()]);
                stake *= Math.exp(-e.weight());
                System.out.printf("= %10.5f %s\n", stake, name[e.to()]);
            }

            return true;
        } else {
//            System.out.println(new Date() + ": No arbitrage opportunity");
        }

        return false;
    }

    /**
     * Reads the currency exchange table from standard input and prints an
     * arbitrage opportunity to standard output (if one exists).
     */
    public static void main(String[] args) {

        Map<String, List<Double>> ratesByCurrency = Maps.newLinkedHashMap();


        ratesByCurrency.put("BTC", Lists.newArrayList(1.0, 378.49, 355.0, 105.05, 434.0));
        ratesByCurrency.put("USD", Lists.newArrayList(0.0026, 1.0, .80, .27, 1.2));
        ratesByCurrency.put("EUR", Lists.newArrayList(0.0028, 1.2, 1.0, 3.25, 0.75));
        ratesByCurrency.put("LTC", Lists.newArrayList(0.0032, 1.4, 3.4, 1.0, 4.8));
        ratesByCurrency.put("ETH", Lists.newArrayList(-1.0, .88, 0.82, 0.60, 1.0));

        int V = ratesByCurrency.size();
        String[] name = new String[V];

        // create complete network
        EdgeWeightedDigraph G = new EdgeWeightedDigraph(V);



        Iterator<String> iterator = ratesByCurrency.keySet().iterator();
        int v = 0;
        while (iterator.hasNext()) {
            String currency = iterator.next();
            name[v] = currency;
            List<Double> rates = ratesByCurrency.get(currency);
            for (int w = 0; w < rates.size(); w++) {
                if (rates.get(w) != -1.0) {
                    DirectedEdge de = new DirectedEdge(v, w, -Math.log(rates.get(w)));
                    G.addEdge(de);
                }
            }
        }

//        for (int v = 0; v < V; v++) {
//            name[v] = StdIn.readString();
//            for (int w = 0; w < V; w++) {
//                double rate = StdIn.readDouble();
//                DirectedEdge e = new DirectedEdge(v, w, -Math.log(rate));
//                G.addEdge(e);
//            }
//        }

        // find negative cycle
        BellmanFordSP spt = new BellmanFordSP(G, 0);
        if (spt.hasNegativeCycle()) {
            double stake = 1000.0;
            for (DirectedEdge e : spt.negativeCycle()) {
                System.out.printf("%10.5f %s ", stake, name[e.from()]);
                stake *= Math.exp(-e.weight());
                System.out.printf("= %10.5f %s\n", stake, name[e.to()]);
            }
        } else {
            System.out.println("No arbitrage opportunity");
        }
    }
}
