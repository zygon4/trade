
package com.zygon.data1.provider;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xeiam.xchange.bitstamp.BitstampExchange;
import com.xeiam.xchange.coinbase.CoinbaseExchange;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.kraken.KrakenExchange;
import com.zygon.data1.Data;
import com.zygon.data1.DataContract;
import com.zygon.data1.DataProvider;
import com.zygon.data1.DataProvider.DataHandler;
import com.zygon.data1.contract.MarketDataContract;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class XChangeProviderTester {

    private static void start (DataProvider provider, Collection<MarketDataContract> contracts) throws Exception {
        provider.connect();

        for (DataContract contract : contracts) {
            provider.start(contract);
        }
    }

    private static void stop (DataProvider provider, Collection<MarketDataContract> contracts) throws Exception {
        for (DataContract contract : contracts) {
            provider.stop(contract);
        }

        provider.disConnect();
    }

    private static final class TickerHandler implements DataHandler<MarketDataContract> {

        @Override
        public void handle(MarketDataContract contract, Data data) {
            System.out.println("received data [" + data + "] from " + contract.getContractName() + "|"+ contract.getDataIdentifier());
        }
    }

    private static final Class<?>[] XCHANGE_BTC_EXCHANGES = {
        BitstampExchange.class,
        CoinbaseExchange.class,
        KrakenExchange.class
    };

    private static DataProvider createProvider(Class<?> exchangeClazz, DataHandler<MarketDataContract> dataHandler, Collection<CurrencyPair> currenciesPerExchange) {
        Map<MarketDataContract,Set<DataHandler<MarketDataContract>>> handlersByContract = Maps.newHashMap();

        for (CurrencyPair pair : currenciesPerExchange) {
            handlersByContract.put(
                new MarketDataContract(exchangeClazz.getName(), pair.baseSymbol, MarketDataContract.MarketDataElement.TICKER, pair),
                Sets.newHashSet(dataHandler));
        }

        return new XChangeProvider(exchangeClazz.getName(), handlersByContract, exchangeClazz.getTypeName());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        TickerHandler handler = new TickerHandler();

        DataProvider[] providers = new DataProvider[XCHANGE_BTC_EXCHANGES.length];
        Collection<CurrencyPair> currenciesPerExchange = Lists.newArrayList(
                CurrencyPair.BTC_USD, CurrencyPair.BTC_EUR, CurrencyPair.BTC_AUD
        );

        for (int i = 0; i < XCHANGE_BTC_EXCHANGES.length; i++) {
            providers[i] = createProvider(XCHANGE_BTC_EXCHANGES[i], handler, currenciesPerExchange);
        }

        try {
            for (int i = 0; i < providers.length; i++) {
                start(providers[i], providers[i].getContracts());
            }

            Thread.sleep (TimeUnit.SECONDS.toMillis(60));

            for (int i = 0; i < providers.length; i++) {
                stop(providers[i], providers[i].getContracts());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
