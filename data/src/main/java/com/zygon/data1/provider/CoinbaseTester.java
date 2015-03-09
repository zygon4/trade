
package com.zygon.data1.provider;

import com.xeiam.xchange.currency.CurrencyPair;
import com.zygon.data1.DataContract;
import com.zygon.data1.DataProvider;
import com.zygon.data1.contract.MarketDataContract;

/**
 *
 * @author zygon
 */
public class CoinbaseTester {

    private static void start (DataProvider provider, DataContract dataContract) throws Exception {
        provider.connect();
        provider.add(dataContract);
        provider.start(dataContract);
    }
    
    private static void stop (DataProvider provider, DataContract dataContract) throws Exception {
        provider.stop(dataContract);
        provider.disConnect();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        // Could (probably should) be a map of contracts to providers.
        // There (by design) can be multiple contracts with the same
        // data provider.
        DataProvider[] providers = new DataProvider[] {
            new CoinBaseXChangeProvider("coinbaseXchange"),
            new KrakenXChangeProvider("krakenXchange")
        };
        
        DataContract[] contracts = new DataContract[] {
            new MarketDataContract("free", "BTC", MarketDataContract.MarketDataElement.TICKER, CurrencyPair.BTC_USD),
            new MarketDataContract("free", "BTC", MarketDataContract.MarketDataElement.TICKER, CurrencyPair.BTC_USD)
        };
        
        for (int i = 0; i < providers.length; i++) {
            start(providers[i], contracts[i]);
        }
        
        Thread.sleep (45000);
        
        for (int i = 0; i < providers.length; i++) {
            stop(providers[i], contracts[i]);
        }
        
        System.exit(0);
    }
}
