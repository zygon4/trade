/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.util.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 *
 * @author zygon
 */
public class Volume extends NumericIndication {

    public static Identifier VOLUME = new ID("volume", Classification.VOLUME);
    
    private final String transactionCurrency;
    
    public Volume(String tradableIdentifier, String transactionCurrency, long timestamp, double value) {
        super(VOLUME, tradableIdentifier, timestamp, value);
        
        this.transactionCurrency = transactionCurrency;
    }

    public String getTransactionCurrency() {
        return this.transactionCurrency;
    }
}
