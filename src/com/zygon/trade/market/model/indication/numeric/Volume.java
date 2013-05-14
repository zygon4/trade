/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class Volume extends NumericIndication {

    private final String transactionCurrency;
    
    public Volume(String tradableIdentifier, String transactionCurrency, long timestamp, double value) {
        super(NumericIndication.IDS.VOLUME, tradableIdentifier, Classification.VOLUME, timestamp, value);
        
        this.transactionCurrency = transactionCurrency;
    }

    public String getTransactionCurrency() {
        return this.transactionCurrency;
    }
}
