/**
 * 
 */

package com.zygon.exchange.market.model.indication.logic.message;

/**
 *
 * @author zygon
 */
public class Support extends SimpleMarketIndication {

    public Support(String tradableIdentifier, String id, long timestamp, double value) {
        super(tradableIdentifier, id, timestamp, Type.SUPPORT, value);
    }
}
