
package com.zygon.data1.contract;

import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data1.DataContract;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author zygon
 */
public class MarketDataContract extends DataContract {

    public static enum MarketDataElement {
        TICKER
    }
    
    private final MarketDataElement marketDataElement;
    private final CurrencyPair currency;

    public MarketDataContract(String contractName, String dataIdentifier, int cacheTime, TimeUnit cacheTimeUnits, 
            MarketDataElement contractData, CurrencyPair currency) {
        super(contractName, dataIdentifier, cacheTime, cacheTimeUnits);
        this.marketDataElement = contractData;
        this.currency = currency;
    }

    public MarketDataContract(String contractName, String dataIdentifier, MarketDataElement contractData, CurrencyPair currency) {
        super(contractName, dataIdentifier);
        this.marketDataElement = contractData;
        this.currency = currency;
    }

    public final MarketDataElement getMarketDataElement() {
        return this.marketDataElement;
    }

    public final String getSymbol() {
        return this.currency.baseSymbol;
    }
    
    public final String getCounterSymbol() {
        return this.currency.counterSymbol;
    }

    public CurrencyPair getCurrency() {
        return this.currency;
    }
}
