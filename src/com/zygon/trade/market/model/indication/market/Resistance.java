/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;
import com.zygon.trade.market.model.indication.ID;
import com.zygon.trade.market.model.indication.Identifier;
import com.zygon.trade.market.model.indication.numeric.NumericIndication;

/**
 *
 * @author zygon
 */
public class Resistance extends NumericIndication {

    public static Identifier RESISTANCE = new ID("resistance", Classification.PRICE);
    
    public Resistance(String tradableIdentifier, long timestamp, double level) {
        super(RESISTANCE, tradableIdentifier, timestamp, level);
    }
}
