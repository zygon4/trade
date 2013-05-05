/**
 * 
 */

package com.zygon.exchange.market.model.indication.logic.message;

/**
 *
 * @author zygon
 */
public class Resistance extends SimpleMarketIndication {

    public Resistance(String tradableIdentifier, String id, long timestamp, double level) {
        super(tradableIdentifier, id, timestamp, Type.RESISTANCE, level);
    }
}
