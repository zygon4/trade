
package com.zygon.analysis.correlation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xeiam.xchange.bitstamp.BitstampExchange;
import com.xeiam.xchange.coinbase.CoinbaseExchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.kraken.KrakenExchange;
import com.zygon.analysis.core.ExchangeTester;
import com.zygon.data1.DataProvider;
import com.zygon.data1.contract.MarketDataContract;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class Tester extends ExchangeTester {

    public Tester(Collection<DataProvider> providers) {
        super(providers);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        CorrelationDataHandler correlationHandler = new CorrelationDataHandler(CurrencyPair.BTC_EUR, KrakenExchange.class.getSimpleName());

        final List<DataProvider> providers = Lists.newArrayList();

        Map<Class<?>,Set<CurrencyPair>> currenciesByExchange = Maps.newHashMap();
        currenciesByExchange.put(BitstampExchange.class, Sets.newHashSet(CurrencyPair.BTC_USD));
        currenciesByExchange.put(CoinbaseExchange.class, Sets.newHashSet(CurrencyPair.BTC_USD)); //CurrencyPair.BTC_AUD, CurrencyPair.BTC_EUR,
        currenciesByExchange.put(KrakenExchange.class, Sets.newHashSet(CurrencyPair.BTC_AUD, CurrencyPair.BTC_EUR, CurrencyPair.BTC_USD));

        currenciesByExchange.entrySet().forEach(e -> {
            providers.add(ExchangeTester.createProvider(e.getKey(), correlationHandler,
                    MarketDataContract.MarketDataElement.TICKER, e.getValue()));
        });

        Tester tester = new Tester(providers);

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
