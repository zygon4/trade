
package com.zygon.data1.provider;

import com.google.common.collect.ImmutableSet;
import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.currency.CurrencyPair;
import com.xeiam.xchange.kraken.dto.trade.KrakenStandardOrder;
import com.xeiam.xchange.kraken.dto.trade.KrakenType;
import com.xeiam.xchange.service.polling.marketdata.PollingMarketDataService;
import com.zygon.data1.Data;
import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.data.OpenOrderBook;
import com.zygon.data1.data.Ticker;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author zygon
 */
public class XChangeProvider extends MarketDataProvider<Exchange> {

    private final String exchangeClazzName;

    public XChangeProvider(String name, Map<MarketDataContract, Set<DataHandler<MarketDataContract>>> dataHandlersByContract, String exchangeClazzName) {
        super(name, dataHandlersByContract);

        this.exchangeClazzName = exchangeClazzName;
    }

    @Override
    protected Exchange createProvider(MarketDataContract dataContract) throws IOException {
        return ExchangeFactory.INSTANCE.createExchange(this.exchangeClazzName);
    }

    @Override
    protected Data getData(Exchange provider, MarketDataContract dataContract) throws IOException {
        Data data = null;

        switch (dataContract.getMarketDataElement()) {
            case TICKER:

                PollingMarketDataService marketDataService = provider.getPollingMarketDataService();

//                String currencies = provider.getPollingMarketDataService().getExchangeSymbols().stream()
//                    .map(CurrencyPair::toString)
//                    .collect(Collectors.joining(",", getName() + " supported currencies: ", ""));
//
//                System.out.println(currencies);

                // Generically speaking, I have no idea what exchanges use which
                // options.
                com.xeiam.xchange.dto.marketdata.Ticker providerTicker = marketDataService.getTicker(dataContract.getCurrency(), true);

//                System.out.println(String.format("Coinbase XChange: ask:[%s], bid[%s] @%s", providerTicker.getAsk(), providerTicker.getBid(), providerTicker.getTimestamp()));

                data = new Ticker(providerTicker, dataContract.getContractName());
                break;
            case BOOK:
                data = new OpenOrderBook(provider.getPollingMarketDataService().getOrderBook(dataContract.getCurrency()));
                BigDecimal volume = BigDecimal.ONE;
                BigDecimal price = BigDecimal.valueOf(355.3);
                BigDecimal stopLoss = price.subtract(price.multiply(BigDecimal.valueOf(0.03)));
                BigDecimal takeProfit = price.add(price.multiply(BigDecimal.valueOf(0.03)));

                KrakenStandardOrder buildOrder = KrakenStandardOrder.getStopLossProfitOrderBuilder(
                        CurrencyPair.BTC_USD, KrakenType.BUY, stopLoss.toPlainString(), takeProfit.toPlainString(), volume)
                        .buildOrder();

                System.out.println(buildOrder);


               break;
            default:
                throw new UnsupportedOperationException(dataContract.getMarketDataElement().name());
        }

        return data;
    }

    // TODO: move to upper layer
    public Set<CurrencyPair> getSupportedCurrencies() throws IOException {
        return ImmutableSet.copyOf(createProvider(null).getPollingMarketDataService().getExchangeSymbols());
    }
}
