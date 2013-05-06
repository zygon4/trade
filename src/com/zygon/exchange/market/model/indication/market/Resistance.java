/**
 * 
 */

package com.zygon.exchange.market.model.indication.market;

import com.zygon.exchange.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class Resistance extends SimpleMarketIndication {

    public Resistance(String tradableIdentifier, long timestamp, double level) {
        super(tradableIdentifier, Classification.PRICE, timestamp, Type.RESISTANCE, level);
    }
}
