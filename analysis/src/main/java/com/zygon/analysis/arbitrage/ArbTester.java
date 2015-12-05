
package com.zygon.analysis.arbitrage;

import com.google.common.collect.Lists;
import com.xeiam.xchange.kraken.KrakenExchange;
import com.zygon.analysis.core.ExchangeTester;
import com.zygon.data1.DataProvider;
import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.provider.XChangeProvider;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class ArbTester extends ExchangeTester {

    public ArbTester(Collection<DataProvider> providers) {
        super(providers);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        ArbitrationDataHandler arbHandler = new ArbitrationDataHandler();

        final List<DataProvider> providers = Lists.newArrayList();

        // ghett0 - create then re-create
        XChangeProvider arbDataProvider = ExchangeTester.createProvider(KrakenExchange.class, arbHandler, MarketDataContract.MarketDataElement.TICKER);
        arbDataProvider = createProvider(KrakenExchange.class, arbHandler, MarketDataContract.MarketDataElement.TICKER, arbDataProvider.getSupportedCurrencies());
        providers.add(arbDataProvider);

        ArbTester tester = new ArbTester(providers);

        try {
            tester.startDataProviders();

            Thread.sleep (TimeUnit.HOURS.toMillis(4));

            tester.stopDataProviders();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
