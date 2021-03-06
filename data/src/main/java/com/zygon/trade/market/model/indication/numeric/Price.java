/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.util.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;

/**
 * This represents the current price.
 *
 * @author zygon
 */
public class Price extends NumericIndication {

    public static Identifier ID = new ID("price", Classification.PRICE);
    
    private final String currency;
    
    public Price(String tradableIdentifier, long timestamp, double value, String currency) {
        super(ID, tradableIdentifier, timestamp, value);
        
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.getCurrency();
    }
}
