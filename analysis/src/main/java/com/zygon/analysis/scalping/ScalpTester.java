
package com.zygon.analysis.scalping;


import com.google.common.collect.Lists;
import com.xeiam.xchange.currency.CurrencyPair;
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
public class ScalpTester extends ExchangeTester {

    public ScalpTester(Collection<DataProvider> providers) {
        super(providers);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        ScalperDataHandler handler = new ScalperDataHandler();

        final List<DataProvider> providers = Lists.newArrayList();

        XChangeProvider arbDataProvider = createProvider(KrakenExchange.class, handler, MarketDataContract.MarketDataElement.BOOK,
                Lists.newArrayList(new CurrencyPair("ETH", "BTC")));
        providers.add(arbDataProvider);

        ScalpTester scaleTester = new ScalpTester(providers);

        try {
            scaleTester.startDataProviders();

            Thread.sleep (TimeUnit.HOURS.toMillis(4));

            scaleTester.stopDataProviders();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
