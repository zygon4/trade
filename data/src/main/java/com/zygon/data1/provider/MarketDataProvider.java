
package com.zygon.data1.provider;

import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.Data;
import com.zygon.data1.DataProvider;
import com.zygon.data1.contract.MarketDataContract.MarketDataElement;
import java.io.IOException;

/**
 *
 * @author zygon
 * @param <P> market provider impl
 */
public abstract class MarketDataProvider<P> extends DataProvider<P, MarketDataContract> {
    
    public MarketDataProvider(String name) {
        super(name);
    }

    protected abstract Data get(P provider, MarketDataElement marketData, CurrencyPair currencyPair) throws IOException;
    
    @Override
    protected final Data getData(P provider, MarketDataContract dataContract) throws IOException {
        return this.get(provider, dataContract.getMarketDataElement(), dataContract.getCurrency());
    }
}
