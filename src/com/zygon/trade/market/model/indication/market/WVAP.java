/**
 * 
 */

package com.zygon.trade.market.model.indication.market;

import com.zygon.trade.market.model.indication.Classification;

/**
 *
 * @author zygon
 */
public class WVAP extends SimpleMarketIndication {

    public WVAP(String tradableIdentifier, long timestamp, double vwap) {
        super(MarketIndication.IDS.WVAP, tradableIdentifier, Classification.PRICE, timestamp, vwap);
    }
}
