
package com.zygon.data1.contract;

import com.xeiam.xchange.currency.CurrencyPair;
import java.util.concurrent.TimeUnit;

/**
 * When using the official coinbase SDK
 *
 * @author zygon
 */
public class CoinbaseContract extends MarketDataContract {
    
    // mmm... plain text api keys
    
    private final String accessToken;
    private final String apiKey;
    private final String apiSecret;
    private final String acctId;

    public CoinbaseContract(String contractName, String dataIdentifier, int cacheTime, TimeUnit cacheTimeUnits, MarketDataElement contractData, CurrencyPair currency,
            String accessToken, String apiKey, String apiSecret, String acctId) {
        super(contractName, dataIdentifier, cacheTime, cacheTimeUnits, contractData, currency);
        this.accessToken = accessToken;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.acctId = acctId;
    }

    public CoinbaseContract(String contractName, String dataIdentifier, MarketDataElement contractData, CurrencyPair currency,
            String accessToken, String apiKey, String apiSecret, String acctId) {
        super(contractName, dataIdentifier, contractData, currency);
        this.accessToken = accessToken;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.acctId = acctId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public String getAcctId() {
        return this.acctId;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getApiSecret() {
        return this.apiSecret;
    }
}
