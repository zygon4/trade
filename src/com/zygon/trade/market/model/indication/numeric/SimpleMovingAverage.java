/**
 * 
 */

package com.zygon.trade.market.model.indication.numeric;

import com.zygon.trade.market.model.indication.Identifier;

/**
 *
 * @author zygon
 */
/*pkg*/ class SimpleMovingAverage extends NumericIndication {

    /*pkg*/ SimpleMovingAverage(Identifier id, String tradableIdentifier, long timestamp, double value) {
        super (id, tradableIdentifier, timestamp, value);
        
        // TODO: what up front checking?
    }
}
