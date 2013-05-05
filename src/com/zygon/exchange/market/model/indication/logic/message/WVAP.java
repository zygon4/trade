/**
 * 
 */

package com.zygon.exchange.market.model.indication.logic.message;

/**
 *
 * @author zygon
 */
public class WVAP extends SimpleMarketIndication {

    public WVAP(String tradableIdentifier, String id, long timestamp, double vwap) {
        super(tradableIdentifier, id, timestamp, Type.WVAP, vwap);
    }
}
