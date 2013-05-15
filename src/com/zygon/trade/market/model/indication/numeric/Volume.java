/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 *
 * @author zygon
 */
public class Volume extends NumericIndication {

    public static Identifier VOLUME = new ID("volume", Classification.VOLUME, null);
    
    private final String transactionCurrency;
    
    public Volume(String tradableIdentifier, String transactionCurrency, long timestamp, double value) {
        super(VOLUME, tradableIdentifier, Classification.VOLUME, timestamp, value);
        
        this.transactionCurrency = transactionCurrency;
    }

    public String getTransactionCurrency() {
        return this.transactionCurrency;
    }
}
