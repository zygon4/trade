package com.zygon.analysis.core;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data1.DataContract;
import com.zygon.data1.DataProvider;
import com.zygon.data1.DataProvider.DataHandler;
import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.provider.XChangeProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author zygon
 */
public class ExchangeTester {

    private final List<DataProvider> providers = Lists.newArrayList();

    public ExchangeTester(Collection<DataProvider> providers) {
        this.providers.addAll(providers);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                super.setDaemon(true);
                try {
                    stopDataProviders();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void start(DataProvider provider, Collection<MarketDataContract> contracts) throws Exception {
        provider.connect();

        for (DataContract contract : contracts) {
            provider.start(contract);
        }
    }

    private void stop(DataProvider provider, Collection<MarketDataContract> contracts) throws Exception {
        for (DataContract contract : contracts) {
            provider.stop(contract);
        }

        provider.disConnect();
    }

    public static XChangeProvider createProvider(Class<?> exchangeClazz, DataHandler<MarketDataContract> dataHandler,
            MarketDataContract.MarketDataElement marketDataElement, Collection<CurrencyPair> currenciesPerExchange) {
        Map<MarketDataContract, Set<DataHandler<MarketDataContract>>> handlersByContract = Maps.newHashMap();

        for (CurrencyPair pair : currenciesPerExchange) {
            handlersByContract.put(
                    new MarketDataContract(exchangeClazz.getSimpleName() + "_" + pair.toString(),
                            pair.baseSymbol, marketDataElement, pair),
                    Sets.newHashSet(dataHandler));
        }

        return new XChangeProvider(exchangeClazz.getName(), handlersByContract, exchangeClazz.getTypeName());
    }

    public static XChangeProvider createProvider(Class<?> exchangeClazz,
            DataHandler<MarketDataContract> dataHandler, MarketDataContract.MarketDataElement marketDataElement) {
        return createProvider(exchangeClazz, dataHandler, marketDataElement, Collections.emptyList());
    }

    public void startDataProviders() throws Exception {
        providers.forEach(p -> {
            try {
                start(p, p.getContracts());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    public void stopDataProviders() throws Exception {
        providers.forEach(p -> {
            try {
                stop(p, p.getContracts());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
