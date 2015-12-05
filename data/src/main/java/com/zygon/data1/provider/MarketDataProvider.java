
package com.zygon.data1.provider;

import com.zygon.data1.contract.MarketDataContract;
import com.zygon.data1.DataProvider;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author zygon
 * @param <P> market provider impl
 */
public abstract class MarketDataProvider<P> extends DataProvider<P, MarketDataContract> {

    public MarketDataProvider(String name, Map<MarketDataContract, Set<DataHandler<MarketDataContract>>> dataHandlersByContract) {
        super(name, dataHandlersByContract);
    }
}
